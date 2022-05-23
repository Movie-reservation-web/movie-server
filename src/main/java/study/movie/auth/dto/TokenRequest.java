package study.movie.auth.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TokenRequest {

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;
}
