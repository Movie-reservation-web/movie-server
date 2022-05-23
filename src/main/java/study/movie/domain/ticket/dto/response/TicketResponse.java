package study.movie.domain.ticket.dto.response;

import lombok.Builder;
import lombok.Data;
import study.movie.domain.ticket.entity.Ticket;

@Data
@Builder
public class TicketResponse {

    public static TicketResponse of(Ticket ticket) {
        return TicketResponse.builder()
                .build();
    }
}
