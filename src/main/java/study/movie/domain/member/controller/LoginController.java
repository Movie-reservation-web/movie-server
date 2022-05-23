package study.movie.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.movie.auth.dto.TokenRequest;
import study.movie.auth.dto.TokenResponse;
import study.movie.global.dto.CustomResponse;
import study.movie.domain.member.dto.request.LoginRequest;
import study.movie.domain.member.service.LoginService;

import javax.validation.Valid;

import static study.movie.global.constants.ResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/login")
public class LoginController {

    private final LoginService loginService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse result = loginService.login(request);
        return CustomResponse.success(LOGIN_SUCCESS, result);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@Valid @RequestBody TokenRequest request) {
        TokenResponse result = loginService.reissue(request);
        return CustomResponse.success(REISSUE_TOKEN_SUCCESS, result);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody TokenRequest request) {
        loginService.logout(request);
        return CustomResponse.success(LOGOUT_SUCCESS);
    }

}
