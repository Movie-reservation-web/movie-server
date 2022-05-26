package study.movie.auth.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;
import study.movie.domain.member.entity.SocialType;
import study.movie.exception.CustomException;
import study.movie.exception.ErrorCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class OAuth2AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String DEFAULT_OAUTH2_LOGIN_PATH_PREFIX = "/oauth2/login/*";
    private static final String HTTP_METHOD = "GET";
    private static final String CODE_KEY = "code";
    private static final String STATE_KEY = "state";

    private static final AntPathRequestMatcher DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_OAUTH2_LOGIN_PATH_PREFIX + "*", HTTP_METHOD);

    public OAuth2AuthenticationFilter(OAuth2AuthenticationProvider oAuth2AuthenticationProvider,   //Provider를 등록해주었다. 이는 조금 이따 설명하겠다.
                                      AuthenticationSuccessHandler authenticationSuccessHandler,  //로그인 성공 시 처리할  handler이다
                                      AuthenticationFailureHandler authenticationFailureHandler) { //로그인 실패 시 처리할 handler이다.

        super(DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER);   // 위에서 설정한  /oauth2/login/* 의 요청에, GET으로 온 요청을 처리하기 위해 설정한다.

        this.setAuthenticationManager(new ProviderManager(oAuth2AuthenticationProvider));
        //AbstractAuthenticationProcessingFilter를 커스터마이징 하려면  ProviderManager를 꼭 지정해 주어야 한다(안그러면 예외남!!!)

        this.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        this.setAuthenticationFailureHandler(authenticationFailureHandler);

    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // social type을 뽑는다.
        SocialType socialType = extractSocialType(request);

        // request에서 파라미터 정보(인가 코드, state)를 뽑는다.
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (values.length > 0) {
                Arrays.stream(values).forEach(value -> paramMap.put(key, value));
            }
        });
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId(request.getRequestURI()
                .substring(DEFAULT_OAUTH2_LOGIN_PATH_PREFIX.length()-1)).build();
        log.info("clientRegistration={}", clientRegistration);
        log.info("clientRegistration={}", clientRegistration.getClientId());
        log.info("clientRegistration={}", clientRegistration.getClientSecret());
        log.info("clientRegistration={}", clientRegistration.getRegistrationId());
        log.info("clientRegistration={}", clientRegistration.getClientName());
        log.info("clientRegistration={}", clientRegistration.getClientAuthenticationMethod());
        log.info("clientRegistration={}", clientRegistration.getRedirectUri());
        log.info("clientRegistration={}", clientRegistration.getProviderDetails());
//        OAuth2LoginAuthenticationToken authenticationRequest = new OAuth2LoginAuthenticationToken(clientRegistration,
//                new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse));
//        authenticationRequest.setDetails(authenticationDetails);
//        OAuth2LoginAuthenticationToken authenticationResult = (OAuth2LoginAuthenticationToken) this
//                .getAuthenticationManager().authenticate(authenticationRequest);
//        OAuth2AuthenticationToken oauth2Authentication = this.authenticationResultConverter
//                .convert(authenticationResult);


        return null;
    }

    private SocialType extractSocialType(HttpServletRequest request) {
        log.info("name={}",request.getRequestURI()
                .substring(DEFAULT_OAUTH2_LOGIN_PATH_PREFIX.length()-1)
                .toUpperCase());
        return Optional.of(
                SocialType.valueOf(
                        request.getRequestURI()
                                .substring(DEFAULT_OAUTH2_LOGIN_PATH_PREFIX.length()-1)
                                .toUpperCase()))
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));
    }

}
