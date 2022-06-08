package study.movie.init;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import study.movie.domain.member.dto.request.SignUpRequest;
import study.movie.domain.member.repository.MemberRepository;
import study.movie.global.utils.JsonUtil;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InitMemberService {
    private static final String MEMBER = "member";
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void initMemberData() {
        memberRepository.saveAll(
                JsonUtil.jsonArrayToList(MEMBER, SignUpRequest.class).stream()
                        .map(signUpRequest -> signUpRequest.toEntity(passwordEncoder))
                        .collect(Collectors.toList()
                        )
        );
    }

}
