package study.movie.global.utils;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.util.StringUtils.hasText;

public abstract class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
    public static final String BEARER_TYPE = "Bearer ";

    /**
     * Request Header에서 토큰 정보 추출
     * <p>
     * ex) 'Bearer AS1DM2LAS47DLA9MSD2OMAWd' 와 같은 토큰이 request의 Authorization부분에 들어있음
     * 그러면 앞에서 부터 7번째(Bearer+공백)를 잘라내면 토큰 정보(뒷 부분)를 가져올 수 있다.
     *
     * @param request
     * @return
     */
    public static String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        return hasBearerToken(bearerToken) ? bearerToken.substring(7) : null;
    }

    public static String extractRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
        return hasText(refreshToken) ? refreshToken : null;
    }

    /**
     * null 체크, 공백 체크, 토큰의 앞 부분이 'Bearer' 로 시작하는지 체크
     *
     * @param bearerToken
     * @return
     */
    private static boolean hasBearerToken(String bearerToken) {
        return hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE);
    }
}
