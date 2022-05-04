package study.movie.repository.ticket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.movie.domain.ticket.Ticket;
import study.movie.dto.ticket.condition.TicketSearchCond;

import java.util.List;

public interface TicketRepositoryCustom {

    List<Ticket> findAllTicketByMember(Long ticketId, Long memberId);

    Long findReservedTicketCountByMember(Long memberId, int year);

    Page<Ticket> search(TicketSearchCond cond, Pageable pageable);
}
