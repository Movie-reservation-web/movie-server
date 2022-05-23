package study.movie.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import study.movie.auth.oauth2.PrincipalDetails;
import study.movie.auth.oauth2.userInfo.OAuth2UserInfo;
import study.movie.auth.oauth2.userInfo.OAuth2UserInfoFactory;
import study.movie.domain.member.entity.Member;
import study.movie.domain.member.entity.SocialType;
import study.movie.domain.member.repository.MemberRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // provider 정보
        SocialType socialType = SocialType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        log.info("socialType={}", socialType);

        // provider 에 맞게 attributes 가공
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(socialType, oAuth2User.getAttributes());

        // 가공된 정보(OAuth2UserInfo)를 가지고 회원 저장 or 연동
        Member member = saveOrConnect(socialType, userInfo);

        // OAuth2User 로 반환
        return new PrincipalDetails(member, userInfo.getAttributes());
    }

    private Member saveOrConnect(SocialType socialType, OAuth2UserInfo userInfo) {
        Member member = memberRepository
                .findByEmail(userInfo.getId())
                .orElse(saveSocialMember(socialType, userInfo));
        if (member.getSocialType() == null) member.connectSocial(socialType);
        return memberRepository.save(member);
    }

    private Member saveSocialMember(SocialType socialType, OAuth2UserInfo userInfo) {
        return Member.socialBuilder()
                .socialType(socialType)
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .build();
    }
}
