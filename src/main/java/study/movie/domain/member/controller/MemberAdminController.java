package study.movie.domain.member.controller;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/members")
public class MemberAdminController {

    private final MemberService memberService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id) {
        MemberResponse result = memberService.findById(id);
        return CustomResponse.success(READ_MEMBER, result);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAdminAccount(@Valid @RequestBody SignUpRequest request) {
        PostIdResponse result = memberService.createAdminAccount(request);
        return CustomResponse.success(CREATED_ADMIN, result);
    }
}
