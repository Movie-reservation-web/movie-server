package study.movie.auth.oauth2.userInfo;

import study.movie.domain.member.entity.SocialType;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {
    private static String NAVER_DOMAIN = "naver.com";

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super((Map<String, Object>) attributes.get("response"));
        super.socialType = SocialType.NAVER;
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        String email = (String) attributes.get("email");
        String[] split = email.split("@");
        return split[1].equals(NAVER_DOMAIN) ? email : split[0] + NAVER_DOMAIN;
    }

    @Override
    public String getImage() {
        return (String) attributes.get("profile_image");
    }

    @Override
    public String getMobile() {
        return (String) attributes.get("mobile");
    }
}
