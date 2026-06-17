package io.github.alreadysolved.mayroom.config.jwt;

import io.github.alreadysolved.mayroom.exception.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.MalformedKeyException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더 추출 (엑세스 토큰이 담긴)
        String accessToken = resolveAccessToken(request);

        if(accessToken != null) { // 토큰이 null이 아닐 경우 검증 후 저장
            try {
                jwtProvider.validateToken(accessToken);

                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) { // ErrorCode로 변경?
                handleJwtException(response, "ACCESS_TOKEN_EXPIRED", "토큰이 만료되었습니다.");
                return; // return을 붙이면, 다음 필터로 넘어가지 않고
            } catch (SignatureException e) {
                handleJwtException(response, "INVALID_ACCESS_TOKEN", "토큰이 변조되었습니다.");
                return;
            } catch (MalformedKeyException e) {
                handleJwtException(response, "INVALID_ACCESS_TOKEN", "유효하지 않은 구성의 토큰입니다.");
                return;
            } catch (UnsupportedJwtException e) {
                handleJwtException(response, "INVALID_ACCESS_TOKEN", "지원하지 않는 형식의 토큰입니다.");
                return;
            } catch (IllegalArgumentException e) { // 토큰 값이 null이거나 완전히 비어있을 때 ("" 또는 " ")
                handleJwtException(response, "INVALID_ACCESS_TOKEN", "토큰이 비어있거나 잘못되었습니다.");
                return;
            }

        }

        filterChain.doFilter(request, response);
    }

    // Access Token은 헤더에 담겨오고, Refresh Token은 요청 Body에 옴
    private String resolveAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        // Authorization 헤더가 존재하고 헤더의 내용이 Bearer 로 시작할 경우
        if(header != null && header.startsWith("Bearer ")) { // ///수정 필요
            return header.substring(7);
        }
        return null;
    }

    private void handleJwtException(HttpServletResponse response, String code, String message) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(code, message);
        String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 에러
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonErrorResponse);

//        response.getWriter().flush(); // 어차피 해당 필터나 컨트롤러가 종료될 때 자동으로 전송되므로 일단 생략
    }
}
