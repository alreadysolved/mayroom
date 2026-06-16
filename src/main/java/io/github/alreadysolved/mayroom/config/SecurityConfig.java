package io.github.alreadysolved.mayroom.config;

import io.github.alreadysolved.mayroom.config.jwt.JwtAccessDeniedHandler;
import io.github.alreadysolved.mayroom.config.jwt.JwtAuthenticationEntryPoint;
import io.github.alreadysolved.mayroom.config.jwt.JwtFilter;
import io.github.alreadysolved.mayroom.domain.user.User;
import io.github.alreadysolved.mayroom.service.CustomOAuth2UserService;
import io.github.alreadysolved.mayroom.service.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
//    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // csrf 방어 비활성화?
                .csrf(AbstractHttpConfigurer::disable)
                /* 세션 방식 비활성화 */
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // .permitAll()은 누구나 접근 가능한 페이지
                // .authenticated()는 SecurityContextHolder에 AnonymousAuthenticationToken이 아닌 Authentication이 들어있으면 통과. Role은 상관없음.
                // AuthorizationFilter(맨 마지막 필터) 에서 ROLE_GUEST가 있는지 검사함.
                // 만약 Authentication이 아예 없으면(=AnonymousAuthenticationToken이면) AuthenticationEntryPoint 호출 -> 401 Unauthorized 응답
                // AnonymousAuthenticationToken이 아닌 Authentication은 있는데 USER가 아니면 AccessDeniedHandler 호출 -> 403 Forbidden 응답
                .authorizeHttpRequests(auth -> auth
                        // 🎯 모든 URL에 대한 OPTIONS(Preflight) 요청은 토큰이 없어도 무조건 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 1. 공통 접근 가능 구역 (토큰 없어도 패스)
                        .requestMatchers("/api/public/**").permitAll()
                        // 2. 추가 정보 입력 구역 (오직 로그인한 유저 중 GUEST 권한만 가능)
                        .requestMatchers("/api/user/auth/extra-info").hasRole("GUEST")
                        // 3. 정식 회원 USER 권한만 가능
                        .requestMatchers("/api/user/**").hasRole("USER")
                        // 4. 일반 로그인 사용자 전용 구역 (USER, GUEST 등)
                        .anyRequest().authenticated())
                // 1. hasRole에서 익명 사용자(=AnonymousAuthenticationToken)가 걸렸을 때 jwtAuthenticationEntryPoint 발동
                // 2. 익명 사용자는 아니지만 role이 부족한 경우에 jwtAccessDeniedHandler 발동
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                // OAuth2 설정
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOidcUserService))
                        .successHandler(authenticationSuccessHandler))
                //
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 🎯 기존 WebConfig에 있던 설정값 100% 그대로 이식
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setExposedHeaders(Collections.singletonList("Authorization")); // 👈 이거 아주 중요했죠!
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 API 경로에 적용
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
