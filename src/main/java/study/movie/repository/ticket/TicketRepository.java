package study.movie.repository.ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.ticket.Ticket;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long>,TicketRepositoryCustom {

    Optional<Ticket> findByReserveNumber(String reserveNumber);

}
