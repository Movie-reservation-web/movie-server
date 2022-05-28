package study.movie.auth.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import study.movie.auth.dto.TokenResponse;
import study.movie.auth.jwt.JwtTokenProvider;
import study.movie.global.utils.JwtUtil;
import study.movie.redis.RedisRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static study.movie.domain.member.entity.Role.GUEST;


@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;
    private final RedisRepository redisRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (isGuest(authentication)) {
            // 프론트에 회원가입을 해야된다고 알려줘야함
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080")
                    .path("/api/v1/categories/film-rating")
                    .encode()
                    .build().toUriString();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            // 메인 화면으로 가면됨
            TokenResponse tokenResponse = tokenProvider.generateToken(authentication);
            redisRepository.save(tokenProvider.getRefreshTokenKey(authentication), tokenResponse);
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080")
                    .path("/")
                    .queryParam("access-token", tokenResponse.getAccessToken())
                    .queryParam("token-type", JwtUtil.BEARER_TYPE)
                    .build().toUriString();
            response.addCookie(tokenResponse.getRefreshTokenCookie());
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }

    private boolean isGuest(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> Objects.equals(grantedAuthority.toString(), GUEST.getValue()));
    }
}
