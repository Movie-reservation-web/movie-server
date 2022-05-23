package study.movie.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;
import study.movie.auth.dto.TokenResponse;
import study.movie.auth.jwt.JwtTokenProvider;
import study.movie.redis.RedisRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static study.movie.domain.member.entity.Role.GUEST;


@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final JwtTokenProvider tokenProvider;
    private final RedisRepository redisRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        TokenResponse tokenResponse = tokenProvider.generateToken(authentication);
        redisRepository.save(tokenProvider.getRefreshTokenKey(authentication), tokenResponse);

        if (checkGuest(authentication)){
            // 회원 가입
        }else {
            // 메인 화면 리턴
        }
    }

    private boolean checkGuest(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> Objects.equals(grantedAuthority.toString(), GUEST.getGrantedAuthority()));
    }
}
