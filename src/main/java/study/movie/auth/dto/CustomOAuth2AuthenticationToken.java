package study.movie.auth.dto;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

import java.util.Collections;

@Getter
public class CustomOAuth2AuthenticationToken extends AbstractAuthenticationToken {
    private OAuth2User principal;
    private ClientRegistration clientRegistration;
    private String accessToken;

    public CustomOAuth2AuthenticationToken(ClientRegistration clientRegistration, String accessToken) {
        super(Collections.emptyList());
        Assert.notNull(clientRegistration, "clientRegistration cannot be null");
        this.clientRegistration = clientRegistration;
        this.accessToken = accessToken;
        this.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
