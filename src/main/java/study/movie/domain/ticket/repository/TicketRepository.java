package study.movie.domain.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.ticket.entity.Ticket;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketRepositoryCustom {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from ticket where ticket.ticket_id in :ids and ticket_status='CANCEL'")
    void deleteAllByIdInQuery(@Param("ids") List<Long> ids);
}
