package study.movie.dto.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.ticket.TicketStatus;

import java.time.LocalDate;

@Data
public class TicketSearchCond {

    private String reserveNumber;
    private String scheduleNumber;
    private String theaterName;
    private ScreenFormat format;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate screenDateStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate screenDateEnd;
    private TicketStatus ticketStatus;
    private String memberName;
    private String movieTitle;
}
