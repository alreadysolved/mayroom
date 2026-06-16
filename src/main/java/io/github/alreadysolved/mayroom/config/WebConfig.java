package io.github.alreadysolved.mayroom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 1. 모든 API 주소에 대해 CORS 허용
                .allowedOrigins("http://localhost:5173") // 2. 프론트엔드 포트 정확히 명시 (본인 포트에 맞게 수정)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 3. OPTIONS(Preflight) 메서드 필수 허용!
                .allowedHeaders("*") // 4. Authorization을 포함한 모든 헤더 허용!
                .exposedHeaders("Authorization") // 5. 프론트가 헤더를 읽을 수 있게 노출
                .allowCredentials(true); // 6. 토큰/쿠키 보안 연동 허용
    }
}
