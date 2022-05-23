package study.movie.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.domain.member.dto.request.SignUpRequest;
import study.movie.domain.member.service.MemberService;

import javax.validation.Valid;

import static study.movie.global.constants.ResponseMessage.CREATED_USER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request) {
        PostIdResponse result = memberService.signup(request);
        return CustomResponse.success(CREATED_USER, result);
    }
}
