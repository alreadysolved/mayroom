package io.github.alreadysolved.mayroom.config.jwt;

import io.github.alreadysolved.mayroom.domain.CustomUserDetails;
import io.github.alreadysolved.mayroom.domain.user.Role;
import io.github.alreadysolved.mayroom.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private final Key key;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-milliseconds}") long accessTokenValidityInMilliseconds,
            @Value("${jwt.refresh-token-validity-in-milliseconds}") long refreshTokenValidityInMilliseconds
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
    }

    // 로그인 시에 실행됨
    // Access Token + Refresh Token 들어있는 TokenResponse 생성하여 반환
    // 로그인 시: authentication은 OAuth2LoginAuthenticationFilter(정확히는 Provider)가 OAuth2UserService.loadUser로 반환받은 OAuth2User(실제로는 CustomUserDetails) 객체를 authentication의 principal에 넣어서 줌
    // authentication.getAuthorities()를 하면 OAuth2User/UserDetails의 getAuthorities를 한 결과가 들어가있음
    public TokenDto issueTokens(Authentication authentication) {
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        String email = (String) oAuth2User.getAttribute("email");

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Long id = principal.getId();

        // 권한 정보를 ,로 구분한 String으로 변환
        // ,로 구분한 이유는 나중에 여러개의 역할을 가질 수도 있을까봐
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return generateTokens(id, authorities);
    }

    // 토큰 생성
    private TokenDto generateTokens(Long id, String authorities) {
        String accessToken = Jwts.builder()
                .claim("id", id) // Long 그대로 넣어도 Jackson 라이브러리가 JSON에 "id": 42 로 넣어줌
//                .claim("email", email)
                .claim("auth", authorities)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenValidityInMilliseconds))
                .signWith(key)
                .compact();

        String refreshToken = Jwts.builder()
                .claim("id", id)
//                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenValidityInMilliseconds))
                .signWith(key)
                .compact();

        return new TokenDto(accessToken, refreshToken);
    }

    public void validateToken(String token) {
        Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token);
    }

    // 현재는 Refresh Token에만 사용
    public Long extractId(String token) {
        Claims claims = parseClaims(token);

        return claims.get("id", Long.class);
    }

    // 토큰에서 만료 시간을 읽어 Date -> Instant -> LocalDateTime으로 반환
    public LocalDateTime extractExpiration(String token) {
        Date expiration = parseClaims(token).getExpiration();

        return expiration.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    // 엑세스 토큰의 내용을 기반으로 Authentication 객체 생성
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        // CustomUserDetails에 넣기 위한 가짜 User(entity) 생성.
        // 매 요청마다 DB에 접근하기 어려우므로 최소한의 정보만 담기 위해서 사용함.
        User user = User.builder()
                .id(claims.get("id", Long.class))
//                .email(claims.get("email").toString())
                .role(Role.valueOf(claims.get("auth").toString()))
                .build();

        CustomUserDetails principal = new CustomUserDetails(user, null);

        // 꼭 principal 필드에 CustomUserDetails를 넣어야 할까? email이나 id를 넣는 건 어떨까?
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    // 토큰을 받아서, 토큰의 구성요소(헤더, 내용, 서명) 중 내용을 반환
    // 리프레시 토큰도 parse할 일이 있나..?
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 새로운 Access Token + Refresh Token을 발급
    public TokenDto reissueTokens(User user) {
        Long id = user.getId();
        String authorities = user.getRole().toString();

        return generateTokens(id, authorities);
    }

}
