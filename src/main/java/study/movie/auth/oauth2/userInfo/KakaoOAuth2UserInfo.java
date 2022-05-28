package study.movie.auth.oauth2.userInfo;

import lombok.Getter;
import study.movie.domain.member.entity.SocialType;

import java.util.Map;

@Getter
public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
    private Map<String, Object> accounts;
    private Map<String, Object> profile;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        super.socialType = SocialType.KAKAO;
        this.accounts = (Map<String, Object>) super.attributes.get("kakao_account");
        this.profile = (Map<String, Object>) accounts.get("profile");
    }

    @Override
    public String getId() {
        return Long.toString((Long) attributes.get("id"));
    }

    @Override
    public String getName() {
        return (String) profile.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) accounts.get("email");
    }

    @Override
    public String getImage() {
        return (String) profile.get("profile_image_url");
    }

    @Override
    public String getMobile() {
        return "";
    }
}
