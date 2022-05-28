package study.movie.auth.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.movie.auth.dto.CustomOAuth2AuthenticationToken;
import study.movie.auth.dto.PrincipalDetails;
import study.movie.auth.oauth2.userInfo.OAuth2UserInfo;
import study.movie.domain.member.entity.Member;
import study.movie.domain.member.entity.SocialType;
import study.movie.domain.member.repository.MemberRepository;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2AuthenticationProvider implements AuthenticationProvider {

    private final CustomOauth2UserService userService;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomOAuth2AuthenticationToken authenticationToken = (CustomOAuth2AuthenticationToken) authentication;
        OAuth2UserInfo userInfo = userService.loadUser(authenticationToken);

        // 가공된 정보(OAuth2UserInfo)를 가지고 회원 저장 or 연동
        Member member = saveOrConnect(userInfo);
        PrincipalDetails principalDetails = new PrincipalDetails(member, userInfo.getAttributes());

        return new OAuth2AuthenticationToken(
                principalDetails,
                principalDetails.getAuthorities(),
                authenticationToken.getClientRegistration().getRegistrationId());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomOAuth2AuthenticationToken.class.isAssignableFrom(authentication);
    }

    private Member saveOrConnect(OAuth2UserInfo userInfo) {
        SocialType socialType = userInfo.getSocialType();
        Member member = memberRepository
                .findByEmail(userInfo.getEmail())
                .orElseGet(() -> saveSocialMember(userInfo));

        if (member.getSocialType() == null) member.connectSocial(socialType);
        return member;
    }

    private Member saveSocialMember(OAuth2UserInfo userInfo) {
        Member member = Member.socialBuilder()
                .socialType(userInfo.getSocialType())
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .build();
        return memberRepository.save(member);
    }


}
