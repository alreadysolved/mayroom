package io.github.alreadysolved.mayroom.controller;

import io.github.alreadysolved.mayroom.config.jwt.TokenDto;
import io.github.alreadysolved.mayroom.domain.CustomUserDetails;
import io.github.alreadysolved.mayroom.dto.CurrentUserResponse;
import io.github.alreadysolved.mayroom.dto.ExtraInfoRequest;
import io.github.alreadysolved.mayroom.dto.TokenRefreshRequest;
import io.github.alreadysolved.mayroom.dto.TokenResponse;
import io.github.alreadysolved.mayroom.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 현재 사용자의 정보를 반환
    // 로그인된 사용자의 경우: CurrentUserResponse 반환
    // 비로그인 사용자:
    // 회원/비회원 화면 구분용
    @GetMapping("/public/auth/me")
    public ResponseEntity<CurrentUserResponse> getCurrentUser(Authentication authentication) { // 스프링이 SecurityContextHolder에서 주입해줌
        CurrentUserResponse currentUserResponse = authService.getAuthUser(authentication);
        return ResponseEntity.ok(currentUserResponse);
    }

    @PostMapping("/user/auth/extra-info") // @AuthenticationPrincipal CustomUserDetails customUserDetails,
    public ResponseEntity<TokenResponse> registerExtraInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                  @RequestBody ExtraInfoRequest extraInfoRequest) {
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        TokenResponse tokenResponse = authService.registerExtraInfo(customUserDetails.getUser(), extraInfoRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    // Refresh Token을 받아 새로운 Access Token + Refresh Token을 생성하여 반환
    @PostMapping("/public/auth/refresh")
    public ResponseEntity<TokenResponse> refreshTokens(@RequestBody TokenRefreshRequest tokenRefreshRequest) {
        TokenResponse tokenResponse = authService.refreshTokens(tokenRefreshRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/user/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        authService.logout(customUserDetails.getId());

        return ResponseEntity.ok().build();
    }

}
