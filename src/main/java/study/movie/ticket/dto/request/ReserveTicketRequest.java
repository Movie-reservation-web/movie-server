package study.movie.ticket.dto.request;

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
    @NotNull
    private Integer price;
}
