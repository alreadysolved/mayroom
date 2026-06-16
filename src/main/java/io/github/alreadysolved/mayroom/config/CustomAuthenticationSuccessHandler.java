package io.github.alreadysolved.mayroom.config;

import io.github.alreadysolved.mayroom.config.jwt.JwtProvider;
import io.github.alreadysolved.mayroom.config.jwt.TokenDto;
import io.github.alreadysolved.mayroom.domain.CustomUserDetails;
import io.github.alreadysolved.mayroom.domain.auth.RefreshToken;
import io.github.alreadysolved.mayroom.repository.auth.RefreshTokenRepository;
import io.github.alreadysolved.mayroom.repository.user.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Authentication 안에는 Service에서 넘겨준 OicdUser 또는 OAuth2User(실제로는 CustomUserDetails)가 들어가 있음
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // Access Token + Refresh Token 생성
        TokenDto tokenDto = jwtProvider.issueTokens(authentication);

        String accessTokenValue = tokenDto.getAccessToken();
        String refreshTokenValue = tokenDto.getRefreshToken();

        /* refreshToken 저장 */
        Long userId = customUserDetails.getId();

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .tokenValue(refreshTokenValue)
                .expiredAt(jwtProvider.extractExpiration(refreshTokenValue))
                .build();

        refreshTokenRepository.save(refreshToken);
        /**/

//        String targetUrl;
//        // 신규 가입일 경우(=추가 회원가입 페이지로 이동)
//        if(customUserDetails.isGuest()) {
//            targetUrl = "http://localhost:5173/extra-info"; // 추가 회원정보 입력 페이지
//        } else { // 기존 회원일 경우(=로그인 성공)
//            // 마지막 로그인 시간 업데이트
//            userRepository.updateLastLoginAt(customUserDetails.getId());
//            targetUrl = "http://localhost:5173/oauth/redirect"; // 메인 페이지
//        }

        // url 뒤에 쿼리 파라미터로 Access Token + Refresh Token 붙임
        String finalUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/oauth/redirect")
                .queryParam("accessToken", accessTokenValue)
                .queryParam("refreshToken", refreshTokenValue)
                .queryParam("role", customUserDetails.getRole())
                .build().toUriString();

        // 상황에 맞는 페이지로 리다이렉트
        // 리다이렉트는, 프론트로 돌아가는 게 아니라 브라우저 주소창이 직접 명령 수행
        getRedirectStrategy().sendRedirect(request, response, finalUrl);
    }

}
