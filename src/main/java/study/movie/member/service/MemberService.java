package study.movie.member.service;

import study.movie.member.dto.request.LoginRequest;
import study.movie.member.dto.response.LoginResponse;

public interface MemberService {
    LoginResponse login(LoginRequest request);
}
