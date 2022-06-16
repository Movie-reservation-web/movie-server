package study.movie.domain.ticket.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(description = "예약 취소 모델")
@Data
public class CancelReservationRequest {

    @Schema(description = "회원 이메일", required = true)
    @NotNull
    @JsonProperty("member_email")
    private String memberEmail;

    @Schema(description = "예약 번호", required = true)
    @NotBlank
    @JsonProperty("reserved_number")
    private String reservedNumber;
}
