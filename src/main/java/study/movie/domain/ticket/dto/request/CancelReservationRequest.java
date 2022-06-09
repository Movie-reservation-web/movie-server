package study.movie.domain.ticket.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CancelReservationRequest {

    @NotNull
    @JsonProperty("member_email")
    private String memberEmail;

    @NotBlank
    @JsonProperty("reserved_number")
    private String reservedNumber;
}
