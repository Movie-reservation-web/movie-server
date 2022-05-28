package study.movie.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import study.movie.auth.dto.PrincipalDetails;
import study.movie.global.utils.BasicServiceUtil;
import study.movie.domain.member.repository.MemberRepository;

import static study.movie.exception.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService extends BasicServiceUtil implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * email로 member를 찾은 뒤 UserDetails 객체로 매핑해 리턴
     *
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .map(PrincipalDetails::new)
                .orElseThrow(getExceptionSupplier(MEMBER_NOT_FOUND));
    }
}
