package study.movie.member.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.auth.TokenRequest;
import study.movie.auth.TokenResponse;
import study.movie.global.dto.CustomResponse;
import study.movie.member.dto.request.LoginRequest;
import study.movie.member.service.LoginService;
import study.movie.member.service.MemberService;

import javax.validation.Valid;

import static study.movie.global.constants.ResponseMessage.*;

@Api(tags = "1. Signup / Login")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberApiController {

    private final MemberService memberService;
    private final LoginService loginService;

    @ApiOperation(value = "회원가입", notes = "회원 가입을 합니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(
            @ApiParam(value = "회원 정보", required = true) @Valid @RequestBody LoginRequest request) {
        TokenResponse result = loginService.login(request);
        return CustomResponse.success(LOGIN_SUCCESS, result);
    }


    @ApiOperation(value = "로그인", notes = "이메일로 로그인을 합니다.")
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @ApiParam(value = "로그인 정보", required = true) @Valid @RequestBody LoginRequest request) {
        TokenResponse result = loginService.login(request);
        return CustomResponse.success(LOGIN_SUCCESS, result);
    }

    @ApiOperation(value = "재발급", notes = "토큰을 재발급합니다.")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(
            @ApiParam(value = "토큰 정보", required = true) @Valid @RequestBody TokenRequest request) {
        TokenResponse result = loginService.reissue(request);
        return CustomResponse.success(REISSUE_TOKEN_SUCCESS, result);
    }

    @ApiOperation(value = "로그아웃", notes = "로그아웃을 합니다.")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @ApiParam(value = "토큰 정보", required = true) @Valid @RequestBody TokenRequest request) {
        loginService.logout(request);
        return CustomResponse.success(LOGOUT_SUCCESS);
    }
}
