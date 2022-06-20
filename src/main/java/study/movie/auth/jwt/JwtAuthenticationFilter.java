package study.movie.auth.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import study.movie.exception.CustomException;
import study.movie.global.utils.JwtUtil;
import study.movie.redis.RedisRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static study.movie.exception.ErrorCode.ALREADY_USED_TOKEN;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtProvider;
    private final RedisRepository redisRepository;

    /**
     * 1. Request Header에서 토큰 정보 추출
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
        String accessToken = JwtUtil.extractAccessToken((HttpServletRequest) request);

        // 토큰 정보가 있으면 유효성 검사
        if (accessToken != null && jwtProvider.validateToken(accessToken)) {
            // 블랙리스트에 있는 토큰인지 확인
            if (this.isInBlackList(accessToken)) throw new CustomException(ALREADY_USED_TOKEN);
            // Authentication 객체를 꺼냄
            Authentication authentication = jwtProvider.getAuthentication(accessToken);
            // email, role
            // Security Context 에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    /**
     * Redis 의 블랙 리스트에 Access Token 정보가 있는지 확인
     *
     * @param accessToken
     * @return Boolean
     */
    private Boolean isInBlackList(String accessToken) {
        return redisRepository.hasKey(accessToken);
    }
}
