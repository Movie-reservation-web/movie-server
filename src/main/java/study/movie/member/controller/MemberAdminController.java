package study.movie.member.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.movie.auth.TokenRequest;
import study.movie.global.dto.CustomResponse;
import study.movie.member.service.LoginService;
import study.movie.member.service.MemberService;

import javax.validation.Valid;

import static study.movie.global.constants.ResponseMessage.LOGOUT_SUCCESS;

@Api(tags = "1. Signup / Login")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/members")
public class MemberAdminController {

    private final MemberService memberService;
    private final LoginService loginService;


    @ApiOperation(value = "관리자 권한 부여", notes = "사용자에게 관리자 권한을 부여합니다.")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @ApiParam(value = "토큰 정보", required = true) @Valid @RequestBody TokenRequest request) {
        loginService.logout(request);
        return CustomResponse.success(LOGOUT_SUCCESS);
    }
}
