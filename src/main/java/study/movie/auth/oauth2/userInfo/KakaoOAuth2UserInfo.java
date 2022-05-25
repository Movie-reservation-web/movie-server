package study.movie.auth.oauth2.userInfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo{

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    private Map<String, Object> getProperties() {
        return (Map<String, Object>) super.attributes.get("properties");
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        Map<String, Object> properties = getProperties();
        return properties != null ? (String) properties.get("name") : null;
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        return kakao_account != null ? (String) kakao_account.get("email") : null;
    }

    @Override
    public String getImage() {
        Map<String, Object> properties = getProperties();
        return properties != null ? (String) properties.get("thumbnail_image") : null;
    }
}
