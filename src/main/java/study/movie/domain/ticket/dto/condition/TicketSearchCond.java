package study.movie.domain.ticket.dto.condition;

import lombok.Data;
import study.movie.domain.theater.entity.ScreenFormat;
import study.movie.domain.ticket.entity.TicketStatus;
import study.movie.global.dto.DateRangeCond;

@Data
public class TicketSearchCond extends DateRangeCond {

    private String reserveNumber;
    private String movieTitle;
    private String theaterName;
    private ScreenFormat screenFormat;
    private String memberName;
    private TicketStatus status;
}
