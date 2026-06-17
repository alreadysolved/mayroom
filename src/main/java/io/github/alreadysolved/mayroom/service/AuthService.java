package io.github.alreadysolved.mayroom.service;

import io.github.alreadysolved.mayroom.config.jwt.JwtProvider;
import io.github.alreadysolved.mayroom.config.jwt.TokenDto;
import io.github.alreadysolved.mayroom.domain.CustomUserDetails;
import io.github.alreadysolved.mayroom.domain.auth.RefreshToken;
import io.github.alreadysolved.mayroom.domain.user.Role;
import io.github.alreadysolved.mayroom.domain.user.User;
import io.github.alreadysolved.mayroom.dto.CurrentUserResponse;
import io.github.alreadysolved.mayroom.dto.ExtraInfoRequest;
import io.github.alreadysolved.mayroom.dto.TokenRefreshRequest;
import io.github.alreadysolved.mayroom.dto.TokenResponse;
import io.github.alreadysolved.mayroom.exception.InvalidRefreshTokenException;
import io.github.alreadysolved.mayroom.exception.MissingRefreshTokenException;
import io.github.alreadysolved.mayroom.exception.RefreshTokenExpiredException;
import io.github.alreadysolved.mayroom.exception.RefreshTokenMismatchException;
import io.github.alreadysolved.mayroom.repository.auth.RefreshTokenRepository;
import io.github.alreadysolved.mayroom.repository.user.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.MalformedKeyException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public CurrentUserResponse getAuthUser(Authentication authentication) {
        // isAuthenticated()는 AnonymousAuthenticationToken일 경우 등에서 false 반환
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            // 로그인되지 않은 사용자
            return CurrentUserResponse.builder()
                    .loggedIn(false)
                    .role(null)
                    .nickname(null)
                    .build();
        }

        // 만약 jwtProvider.getAuthentication() 메소드에서 principal에 String을 넣었다면 그걸 그대로 반환,
        // UserDetails를 넣었다면 .getUsername()을 호출한 결과를 반환
//         String email = authentication.getName();

        // 로그인된 사용자

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();

        return CurrentUserResponse.builder()
                .loggedIn(true)
                .role(user.getRole())
                .nickname(user.getNickname())
                .build();
    }

    // 수정 필요?
    public TokenResponse registerExtraInfo(User user, ExtraInfoRequest extraInfoRequest) {
        Long id = user.getId();
        // user는 jwt provider에서 만든 가짜 user 객체이므로 nickname이 null임. 따라서 실제 user를 가져옴
        User realUser = userRepository.findById(id);
        // 추가 회원입력을 마친 회원의 ROLE을 GUEST -> USER로 바꿔줌
        realUser.setRole(Role.ROLE_USER);
        realUser.setNickname(extraInfoRequest.getNickname());
        // 저장소 업데이트
        userRepository.updateExtraInfo(realUser);

        // role이 바뀌었으므로 그에 맞는 새로운 Access Token + Refresh Token 발급
        TokenDto tokenDto = jwtProvider.reissueTokens(realUser);

        // DB에 새 Refresh Token 업데이트
        String newRefreshTokenValue = tokenDto.getRefreshToken();
        RefreshToken newRefreshToken = RefreshToken.builder()
                .userId(id)
                .tokenValue(newRefreshTokenValue)
                .expiredAt(jwtProvider.extractExpiration(newRefreshTokenValue))
                .build();

        refreshTokenRepository.save(newRefreshToken);

        return TokenResponse.from(tokenDto);
    }

    public TokenResponse refreshTokens(TokenRefreshRequest tokenRefreshRequest) {
        // 편의상 Value를 붙임
        String refreshTokenValue = tokenRefreshRequest.getRefreshToken();

        if (refreshTokenValue == null) {
            throw new MissingRefreshTokenException("리프레시 토큰을 전달받지 못했습니다.");
        }

        // 토큰 유효성 검증
        try {
            jwtProvider.validateToken(refreshTokenValue);
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenExpiredException();
        } catch (JwtException e) { // SignatureException, MalformedKeyException, UnsupportedJwtException, IllegalArgumentException
            throw new InvalidRefreshTokenException(e);
        }

        /* DB에 접근하여 사용자 확인 */
        Long id = jwtProvider.extractId(refreshTokenValue);
        RefreshToken savedRefreshToken = refreshTokenRepository.findByUserId(id);

        // DB에 저장된 해당 사용자의 리프레시 토큰과 같지 않다면
        if (!savedRefreshToken.getTokenValue().equals(refreshTokenValue)) {
            throw new RefreshTokenMismatchException("사용자의 리프레시 토큰 정보와 일치하지 않습니다.");
        }

        User user = userRepository.findById(id);

        // 새로운 Access Token + Refresh Token 발급
        TokenDto tokenDto = jwtProvider.reissueTokens(user);

        // DB에 새 Refresh Token 업데이트
        String newRefreshTokenValue = tokenDto.getRefreshToken();
        RefreshToken newRefreshToken = RefreshToken.builder()
                .userId(user.getId())
                .tokenValue(newRefreshTokenValue)
                .expiredAt(jwtProvider.extractExpiration(newRefreshTokenValue))
                .build();

        refreshTokenRepository.save(newRefreshToken);

        return TokenResponse.from(tokenDto);
    }


}
