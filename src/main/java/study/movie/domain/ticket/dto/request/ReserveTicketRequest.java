package study.movie.domain.ticket.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(description = "티켓 예약 정보 모델")
@Data
public class ReserveTicketRequest {
    @Schema(description = "상품 아이디", required = true)
    @NotNull
    @JsonProperty("merchant_uid")
    private String merchantUid;

    @Schema(description = "상영일정 번호", required = true)
    @NotNull
    @JsonProperty("schedule_number")
    private String scheduleNumber;

    @Schema(description = "회원 이메일", required = true)
    @NotNull
    @JsonProperty("member_email")
    private String memberEmail;

    @Schema(description = "좌석", required = true)
    @NotNull
    private List<String> seats;
}
