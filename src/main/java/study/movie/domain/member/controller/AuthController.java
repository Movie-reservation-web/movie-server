package study.movie.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.auth.dto.TokenResponse;
import study.movie.domain.member.dto.request.LoginRequest;
import study.movie.domain.member.service.AuthService;
import study.movie.global.dto.CustomResponse;

import javax.validation.Valid;

import static study.movie.global.constants.ResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse result = authService.login(request);
        return CustomResponse.success(result.getHeaders(), LOGIN_SUCCESS);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(
            @RequestHeader(value = "Refresh-Token") String refreshTokenRequest) {
        TokenResponse result = authService.reissue(refreshTokenRequest);
        return CustomResponse.success(result.getHeaders(), REISSUE_TOKEN_SUCCESS);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestHeader(value = "Authorization") String accessTokenRequest) {
        authService.logout(accessTokenRequest);
        return CustomResponse.success(LOGOUT_SUCCESS);
    }

}
