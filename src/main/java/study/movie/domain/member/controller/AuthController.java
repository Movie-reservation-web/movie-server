package study.movie.domain.member.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import study.movie.auth.dto.AccessTokenResponse;
import study.movie.auth.dto.TokenResponse;
import study.movie.domain.member.dto.request.LoginRequest;
import study.movie.domain.member.dto.request.OAuth2RegisterRequest;
import study.movie.domain.member.service.AuthService;
import study.movie.global.dto.CustomResponse;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static study.movie.global.constants.ResponseMessage.*;

@Api(value = "Auth Controller", tags = "Auth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "일반 로그인", description = "일반 로그인을 한다.")
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        TokenResponse result = authService.login(request);
        response.addCookie(result.getRefreshTokenCookie());
        return CustomResponse.success(LOGIN_SUCCESS, AccessTokenResponse.of(result.getAccessToken()));
    }

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰으로 엑세스 토큰을 재발급한다.")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(
            @ApiIgnore
            @CookieValue(value = "Refresh-Token") String refreshTokenRequest) {
        TokenResponse result = authService.reissue(refreshTokenRequest);
        return CustomResponse.success(REISSUE_TOKEN_SUCCESS, AccessTokenResponse.of(result.getAccessToken()));
    }

    @Operation(summary = "로그아웃", description = "로그아웃을 한다.")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @ApiIgnore
            @RequestHeader(value = "Authorization") String accessTokenRequest) {
        authService.logout(accessTokenRequest);
        return CustomResponse.success(LOGOUT_SUCCESS);
    }

    @Operation(summary = "기본 회원정보 등록", description = "소셜 로그인시 기존에 등록된 회원이 없으면 기본 회원정보를 등록해 회원가입한다.")
    @PostMapping("/login/register")
    public ResponseEntity<?> registerBasicProfile(
            @Valid @RequestBody OAuth2RegisterRequest request,
            HttpServletResponse response) {
        TokenResponse result = authService.socialRegister(request);
        response.addCookie(result.getRefreshTokenCookie());
        return CustomResponse.success(LOGIN_SUCCESS, AccessTokenResponse.of(result.getAccessToken()));
    }
}
