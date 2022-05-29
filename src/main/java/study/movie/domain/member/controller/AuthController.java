package study.movie.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.auth.dto.AccessTokenResponse;
import study.movie.auth.dto.TokenResponse;
import study.movie.domain.member.dto.request.LoginRequest;
import study.movie.domain.member.dto.request.OAuth2RegisterRequest;
import study.movie.domain.member.service.AuthService;
import study.movie.global.dto.CustomResponse;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static study.movie.global.constants.ResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        TokenResponse result = authService.login(request);
        response.addCookie(result.getRefreshTokenCookie());
        return CustomResponse.success(LOGIN_SUCCESS, AccessTokenResponse.of(result.getAccessToken()));
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(
            @CookieValue(value = "Refresh-Token") String refreshTokenRequest) {
        TokenResponse result = authService.reissue(refreshTokenRequest);
        return CustomResponse.success(REISSUE_TOKEN_SUCCESS, AccessTokenResponse.of(result.getAccessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestHeader(value = "Authorization") String accessTokenRequest) {
        authService.logout(accessTokenRequest);
        return CustomResponse.success(LOGOUT_SUCCESS);
    }

    @PostMapping("/login/register")
    public ResponseEntity<?> registerBasicProfile(@Valid @RequestBody OAuth2RegisterRequest request, HttpServletResponse response) {
        TokenResponse result = authService.socialRegister(request);
        response.addCookie(result.getRefreshTokenCookie());
        return CustomResponse.success(LOGIN_SUCCESS, AccessTokenResponse.of(result.getAccessToken()));
    }
}
