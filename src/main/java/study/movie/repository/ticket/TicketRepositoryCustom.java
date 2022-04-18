package study.movie.repository.ticket;

import study.movie.domain.ticket.Ticket;

import java.util.List;

public interface TicketRepositoryCustom {

    List<Ticket> findAllTicketByMember(Long memberId);
}
