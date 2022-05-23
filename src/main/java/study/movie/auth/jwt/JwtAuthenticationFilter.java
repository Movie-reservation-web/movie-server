package study.movie.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import study.movie.redis.RedisRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    private final JwtTokenProvider jwtProvider;
    private final RedisRepository redisRepository;

    /**
     * 1. Request Header에서 토큰 정보 추충
     * 2. 토큰 정보가 있으면 JwtTokenProvider 의 validToken 를 통해 유효성 검사
     * 3. 토큰이 유효하면 토큰에서 Authentication 객체를 Security Context 에 저장
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        // Request Header 에서 토큰 추출
        String accessToken = this.resolveToken((HttpServletRequest) request);

        // 토큰 정보가 있으면 유효성 검사
        if (accessToken != null && jwtProvider.validateToken(accessToken)) {
            if (this.isLogout(accessToken)) { // 블랙리스트 확인
                // Authentication 객체를 꺼냄
                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                // Security Context 에 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * Request Header에서 토큰 정보 추출
     * <p>
     * ex) 'Bearer AS1DM2LAS47DLA9MSD2OMAWd' 와 같은 토큰이 request의 Authorization부분에 들어있음
     * 그러면 앞에서 부터 7번째(Bearer+공백)를 잘라내면 토큰 정보(뒷 부분)를 가져올 수 있다.
     *
     * @param request
     * @return
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        return hasBearerToken(bearerToken) ? bearerToken.substring(7) : null;
    }

    /**
     * Redis 의 블랙 리스트에 Access Token 정보가 있는지 확인
     *
     * @param accessToken
     * @return Boolean
     */
    private Boolean isLogout(String accessToken) {
        return redisRepository.hasKey(accessToken);
    }

    /**
     * null 체크, 공백 체크, 토큰의 앞 부분이 'Bearer' 로 시작하는지 체크
     *
     * @param bearerToken
     * @return
     */
    private boolean hasBearerToken(String bearerToken) {
        return hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE);
    }
}
