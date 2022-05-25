package study.movie.auth.dto;

import lombok.Builder;
import lombok.Getter;

import javax.servlet.http.Cookie;

import static study.movie.global.utils.JwtUtil.REFRESH_TOKEN_HEADER;

@Getter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;

    @Builder
    public TokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Cookie getRefreshTokenCookie() {
        Cookie cookie = new Cookie(REFRESH_TOKEN_HEADER, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        return cookie;
    }
}
