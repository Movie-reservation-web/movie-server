package study.movie.auth.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import study.movie.auth.dto.CustomOAuth2AuthenticationToken;
import study.movie.auth.oauth2.userInfo.OAuth2UserInfo;
import study.movie.auth.oauth2.userInfo.OAuth2UserInfoFactory;
import study.movie.domain.member.entity.SocialType;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
public class CustomOauth2UserService {

    public OAuth2UserInfo loadUser(CustomOAuth2AuthenticationToken token) {
        // 회원 정보를 요청할 uri
        String userInfoUri = token.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();

        // 요청한 회원정보와 매핑된 컬렉션
        Map<String, Object> attributes = this.getOAuth2UserInfo(userInfoUri, token.getAccessToken());
        // provider 정보
        SocialType socialType = this.extractSocialType(token.getClientRegistration().getRegistrationId());

        // provider 에 맞게 attributes 가공
        return OAuth2UserInfoFactory.getOAuth2UserInfo(socialType, attributes);
    }

    private SocialType extractSocialType(String registrationId) {//요청을 처리하는 코드이다
        OAuth2Error oauth2Error = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST);
        return Arrays.stream(SocialType.values())//SocialType.values() -> GOOGLE, KAKAO, NAVER 가 있다.
                .filter(socialType -> socialType.getValue().equals(registrationId))
                .findFirst()
                .orElseThrow(() -> new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString()));

    }

    private Map<String, Object> getOAuth2UserInfo(String userInfoUri, String accessToken) {
        return WebClient.create()
                .get()
                .uri(userInfoUri)
                .headers(header -> header.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }
}
