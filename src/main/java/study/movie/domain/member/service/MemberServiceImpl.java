package study.movie.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.utils.BasicServiceUtil;
import study.movie.domain.member.dto.request.SignUpRequest;
import study.movie.domain.member.entity.Member;
import study.movie.domain.member.repository.MemberRepository;

import static study.movie.domain.member.entity.Role.ADMIN;
import static study.movie.domain.member.entity.Role.USER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl extends BasicServiceUtil implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public PostIdResponse signup(SignUpRequest request) {
        Member member = request.toEntity(passwordEncoder);
        member.conferRole(USER);
        Member savedMember = memberRepository.save(member);
        return PostIdResponse.of(savedMember.getId());
    }

    @Override
    @Transactional
    public PostIdResponse createAdminAccount(SignUpRequest request) {
        Member member = request.toEntity(passwordEncoder);
        member.conferRole(ADMIN);
        Member savedMember = memberRepository.save(member);
        return PostIdResponse.of(savedMember.getId());
    }


}
