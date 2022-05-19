package study.movie.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import study.movie.global.utils.BasicServiceUtil;
import study.movie.member.entity.Member;
import study.movie.member.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

import static study.movie.exception.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService extends BasicServiceUtil implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * email로 member를 찾은 뒤 UserDetails 객체로 매핑해 리턴
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(getExceptionSupplier(MEMBER_NOT_FOUND));
    }

    /**
     * member 정보를 가지고 UserDetails를 만듦
     * @param member
     * @return
     */
    private UserDetails createUserDetails(Member member) {
        List<SimpleGrantedAuthority> authorities = member.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new User(member.getEmail(), member.getPassword(), authorities);
    }
}
