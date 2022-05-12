package study.movie.dto.ticket.condition;

import lombok.Data;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.ticket.TicketStatus;
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
