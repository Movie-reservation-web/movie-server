package study.movie.auth.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
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
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;
    private final RedisRepository redisRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        TokenResponse tokenResponse = tokenProvider.generateToken(authentication);
        redisRepository.save(tokenProvider.getRefreshTokenKey(authentication), tokenResponse);


        if (checkGuest(authentication)) {
            System.out.println("===========회원가입을 진행해주세요============");
            // 프론트에 회원가입을 해야된다고 알려줘야함
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080")
                    .path("/api/v1/categories/film-rating")
                    .build().toUriString();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            // 메인 화면으로 가면됨
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080")
                    .path("/api/v1/categories/city")
                    .build().toUriString();
            response.addCookie(tokenResponse.getRefreshTokenCookie());
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
            // 메인 화면 리턴
        }
    }

    private boolean checkGuest(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> Objects.equals(grantedAuthority.toString(), GUEST.getValue()));
    }
}
