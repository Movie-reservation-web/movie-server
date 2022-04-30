package study.movie.dto.ticket.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ReserveTicketRequest {

    @NotNull
    private Long scheduleId;
    @NotNull
    private Long memberId;
    @NotNull
    private List<String> seats;
}
