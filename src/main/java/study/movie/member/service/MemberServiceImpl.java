package study.movie.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.exception.CustomException;
import study.movie.global.utils.BasicServiceUtil;
import study.movie.member.dto.request.LoginRequest;
import study.movie.member.dto.response.LoginResponse;
import study.movie.member.entity.Member;
import study.movie.member.repository.MemberRepository;

import static study.movie.exception.ErrorCode.EMAIL_MEMBER_NOT_FOUND;
import static study.movie.exception.ErrorCode.INVALID_PASSWORD;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl extends BasicServiceUtil implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(getExceptionSupplier(EMAIL_MEMBER_NOT_FOUND));
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword()))
            throw new CustomException(INVALID_PASSWORD);
        return LoginResponse.of(member);
    }

}
