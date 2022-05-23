package study.movie.auth.oauth2.userInfo;

import study.movie.exception.CustomException;
import study.movie.exception.ErrorCode;
import study.movie.domain.member.entity.SocialType;

import java.util.Map;

/**
 * Social Type에 따라서 해당 소셜에 맞는 UserInfo 객체를 가져온다
 */
public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(SocialType socialType, Map<String, Object> attributes){
        switch (socialType) {
            case KAKAO:
                return new KakaoOAuth2UserInfo(attributes);
            case NAVER:
                return new NaverOAuth2UserInfo(attributes);
            case GOOGLE:
                return new GoogleOAuth2UserInfo(attributes);
            default:
                throw new CustomException(ErrorCode.INVALID_OAUTH2_PROVIDER);
        }
    }
}
