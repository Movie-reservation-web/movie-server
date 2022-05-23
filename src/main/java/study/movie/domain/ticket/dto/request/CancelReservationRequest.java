package study.movie.domain.ticket.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CancelReservationRequest {
    @NotNull
    private Long memberId;
    @NotBlank
    private String reservedNumber;

}
