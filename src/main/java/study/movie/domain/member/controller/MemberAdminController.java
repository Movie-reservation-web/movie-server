package study.movie.domain.member.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.domain.member.dto.request.SignUpRequest;
import study.movie.domain.member.dto.response.MemberResponse;
import study.movie.domain.member.service.MemberService;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.PostIdResponse;

import javax.validation.Valid;

import static study.movie.global.constants.ResponseMessage.CREATED_ADMIN;
import static study.movie.global.constants.ResponseMessage.READ_MEMBER;

@Api(value = "Member Controller", tags = "[Admin] Member")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/members")
public class MemberAdminController {

    private final MemberService memberService;

    @Operation(summary = "회원 찾기", description = "회원 id로 회원을 찾는다.")
    @Parameters({@Parameter(name = "id", description = "회원의 id", required = true, in = ParameterIn.PATH)})
    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id) {
        MemberResponse result = memberService.findById(id);
        return CustomResponse.success(READ_MEMBER, result);
    }

    @Operation(summary = "회원 생성", description = "회원을 생성한다.")
    @PostMapping("/create")
    public ResponseEntity<?> createAdminAccount(@Valid @RequestBody SignUpRequest request) {
        PostIdResponse result = memberService.createAdminAccount(request);
        return CustomResponse.success(CREATED_ADMIN, result);
    }
}
