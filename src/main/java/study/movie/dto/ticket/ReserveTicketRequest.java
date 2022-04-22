package study.movie.dto.ticket;

import lombok.Data;

import java.util.List;

@Data
public class ReserveTicketRequest {

    private Long scheduleId;
    private Long memberId;
    private List<String> seats;

}
