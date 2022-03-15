package study.movie.domain.reserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.reserve.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
