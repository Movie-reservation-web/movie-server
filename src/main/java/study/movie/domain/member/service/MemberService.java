package study.movie.domain.member.service;

import study.movie.global.dto.PostIdResponse;
import study.movie.domain.member.dto.request.SignUpRequest;

public interface MemberService {

    /**
     * Api Server
     * 일반 회원 가입
     */
    PostIdResponse signup(SignUpRequest request);

    /**
     * Admin Server <p>
     * 관리자 권한을 갖는 계정 생성
     *
     * @param request
     */
    PostIdResponse createAdminAccount(SignUpRequest request);
}
