package study.movie.repository.ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.ticket.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
