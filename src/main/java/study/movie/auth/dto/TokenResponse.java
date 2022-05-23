package study.movie.auth.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;

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

    public HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(REFRESH_TOKEN_HEADER, refreshToken);
        headers.setBearerAuth(accessToken);
        return headers;
    }
}
