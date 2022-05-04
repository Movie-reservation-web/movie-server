package study.movie.dto.ticket.condition;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.ticket.TicketStatus;

import java.time.LocalDate;

@Data
public class TicketSearchCond {

    private String reserveNumber;
    private String movieTitle;
    private String theaterName;
    private ScreenFormat screenFormat;
    private String memberName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate screenStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate screenEndDate;

    private TicketStatus status;
}
