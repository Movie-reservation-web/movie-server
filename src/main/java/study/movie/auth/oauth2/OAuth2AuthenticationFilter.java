package study.movie.auth.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import study.movie.auth.dto.CustomOAuth2AuthenticationToken;
import study.movie.auth.dto.OAuth2TokenRequest;
import study.movie.auth.dto.OAuth2TokenResponse;
import study.movie.global.utils.OAuth2Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;


@Component
@Slf4j
public class OAuth2AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String DEFAULT_OAUTH2_LOGIN_PATH_PREFIX = "/oauth2/login/*";
    private static final String HTTP_METHOD = "GET";
    private static final AntPathRequestMatcher DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_OAUTH2_LOGIN_PATH_PREFIX + "*", HTTP_METHOD);

    private final ClientRegistrationRepository clientRegistrationRepository;

    public OAuth2AuthenticationFilter(OAuth2AuthenticationProvider oAuth2AuthenticationProvider,   //Provider를 등록해주었다. 이는 조금 이따 설명하겠다.
                                      OAuth2SuccessHandler successHandler,  //로그인 성공 시 처리할  handler이다
                                      OAuth2FailureHandler failureHandler,
                                      ClientRegistrationRepository clientRegistrationRepository) { //로그인 실패 시 처리할 handler이다.

        super(DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER);   // 위에서 설정한  /oauth2/login/* 의 요청에, GET으로 온 요청을 처리하기 위해 설정한다.

        this.setAuthenticationManager(new ProviderManager(oAuth2AuthenticationProvider));
        //AbstractAuthenticationProcessingFilter를 커스터마이징 하려면  ProviderManager를 꼭 지정해 주어야 한다(안그러면 예외남!!!)

        this.setAuthenticationSuccessHandler(successHandler);
        this.setAuthenticationFailureHandler(failureHandler);
        this.clientRegistrationRepository = clientRegistrationRepository;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // request에서 파라미터 정보(code, state, error)를 뽑는다.
        MultiValueMap<String, String> params = OAuth2Util.toMultiMap(request.getParameterMap());
        // 파라미터 인증.
        if (!OAuth2Util.isAuthorizationResponse(params)) {
            OAuth2Error oauth2Error = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        // registration 정보 가져오기
        String registrationId = this.extractRegistrationId(request);
        ClientRegistration clientRegistration = this.clientRegistrationRepository.findByRegistrationId(registrationId);

        // 토큰을 받기 위한 정보를 담은 request 객체 생성
        OAuth2TokenRequest tokenRequest = OAuth2TokenRequest.of(clientRegistration, params);

        // 토큰을 요청할 uri과 request를 가지고 토큰정보(reponse)를 받음
        String tokenUri = clientRegistration.getProviderDetails().getTokenUri();
        OAuth2TokenResponse tokenResponse = getOAuth2AccessToken(tokenUri, tokenRequest);

        Authentication authenticate = this.getAuthenticationManager().authenticate(new CustomOAuth2AuthenticationToken(clientRegistration, tokenResponse.getAccessToken()));
        return authenticate;
    }

    private String extractRegistrationId(HttpServletRequest request) {
        return request.getRequestURI().substring(DEFAULT_OAUTH2_LOGIN_PATH_PREFIX.length() - 1);
    }

    private OAuth2TokenResponse getOAuth2AccessToken(String userInfoUri, OAuth2TokenRequest tokenRequest) {
        return WebClient.create()
                .post()
                .uri(userInfoUri)
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest.toMultiMap())
                .retrieve()
                .bodyToMono(OAuth2TokenResponse.class)
                .block();
    }
}
