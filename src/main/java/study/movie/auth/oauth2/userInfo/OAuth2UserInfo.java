package study.movie.auth.oauth2.userInfo;

import lombok.Getter;
import study.movie.domain.member.entity.SocialType;

import java.util.Map;

/**
 * Social Type에 맞는 UserInfo를 생성하기 위한 abstract class
 * <p>
 * 공통적으로 들어오는 attributes는 이곳에서 선언하고
 * 내부 값들을 가져오는 메서드는 구현 클래스에서 만들어서 사용
 */
@Getter
public abstract class OAuth2UserInfo {
    protected SocialType socialType;
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImage();

    public abstract String getMobile();
}
