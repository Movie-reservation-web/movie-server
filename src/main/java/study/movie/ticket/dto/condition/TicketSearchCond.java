package study.movie.ticket.dto.condition;

import lombok.Data;
import study.movie.theater.entity.ScreenFormat;
import study.movie.ticket.entity.TicketStatus;
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
