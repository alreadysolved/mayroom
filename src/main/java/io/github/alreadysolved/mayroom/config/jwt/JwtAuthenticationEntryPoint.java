package io.github.alreadysolved.mayroom.config.jwt;

import io.github.alreadysolved.mayroom.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@RequiredArgsConstructor
@Component
// hasRole에 걸렸을 때 익명 사용자인 경우 + authenticated에 걸렸을 때 익명 사용자인 경우 발동
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorResponse errorResponse = new ErrorResponse("ACCESS_TOKEN_MISSING", "엑세스 토큰을 전달받지 못했습니다.");
        String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 이게 없으면 302 에러를 내려줌
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonErrorResponse);

        response.getWriter().flush();
    }
}
