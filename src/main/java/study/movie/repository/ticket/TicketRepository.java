package study.movie.repository.ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.ticket.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketRepositoryCustom {

    @Transactional
    @Modifying
    @Query(value = "delete from Ticket t where t.id in :ids")
    void deleteAllByIdInQuery(@Param("ids") List<Long> ids);
}
