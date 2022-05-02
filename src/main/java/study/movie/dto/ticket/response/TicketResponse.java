package study.movie.dto.ticket.response;

import lombok.Builder;
import lombok.Data;
import study.movie.domain.ticket.Ticket;

@Data
@Builder
public class TicketResponse {

    public static TicketResponse of(Ticket ticket) {
        return TicketResponse.builder()
                .build();
    }
}
