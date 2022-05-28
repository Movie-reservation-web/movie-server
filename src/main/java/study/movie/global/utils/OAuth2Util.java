package study.movie.global.utils;

import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;

public class OAuth2Util {
    public static MultiValueMap<String, String> toMultiMap(Map<String, String[]> map) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(map.size());
        map.forEach((key, values) -> {
            if (values.length > 0) {
                for (String value : values) {
                    params.add(key, value);
                }
            }
        });
        return params;
    }

    public static String getAuthenticationCode(MultiValueMap<String, String> map) {
        return String.valueOf(map.getFirst(OAuth2ParameterNames.CODE));
    }
    public static String getState(MultiValueMap<String, String> map) {
        return String.valueOf(map.getFirst(OAuth2ParameterNames.STATE));
    }

    public static boolean isAuthorizationResponse(MultiValueMap<String, String> request) {
        return isAuthorizationResponseSuccess(request) || isAuthorizationResponseError(request);
    }

    public static boolean isAuthorizationResponseSuccess(MultiValueMap<String, String> request) {
        return StringUtils.hasText(request.getFirst(OAuth2ParameterNames.CODE))
                && StringUtils.hasText(request.getFirst(OAuth2ParameterNames.STATE));
    }

    public static boolean isAuthorizationResponseError(MultiValueMap<String, String> request) {
        return StringUtils.hasText(request.getFirst(OAuth2ParameterNames.ERROR))
                && StringUtils.hasText(request.getFirst(OAuth2ParameterNames.STATE));
    }

}
