package study.movie.auth.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import study.movie.global.utils.OAuth2Util;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.*;

@Builder
@Getter
public class OAuth2TokenRequest {
    private String code;
    private String state;
    private String grantType;
    private String redirectUri;
    private String clientSecret;
    private String clientId;

    public static OAuth2TokenRequest of(ClientRegistration clientRegistration, MultiValueMap<String, String> params) {
        return OAuth2TokenRequest.builder()
                .code(OAuth2Util.getAuthenticationCode(params))
                .state(OAuth2Util.getState(params))
                .grantType("authorization_code")
                .redirectUri(clientRegistration.getRedirectUri())
                .clientSecret(clientRegistration.getClientSecret())
                .clientId(clientRegistration.getClientId())
                .build();
    }

    public MultiValueMap<String, String> toMultiMap() {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add(CODE, code);
        data.add(GRANT_TYPE, grantType);
        data.add(REDIRECT_URI, redirectUri);
        data.add(CLIENT_SECRET, clientSecret);
        data.add(CLIENT_ID, clientId);
        return data;
    }
}
