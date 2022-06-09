package study.movie.domain.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IamportTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    private String now;

    @JsonProperty("expired_at")
    private String expiredAt;

}
