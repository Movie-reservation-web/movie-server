package study.movie.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import study.movie.ticket.entity.Ticket;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketRepositoryCustom {

    @Transactional
    @Modifying
    @Query(value = "delete from Ticket t where t.id in :ids")
    void deleteAllByIdInQuery(@Param("ids") List<Long> ids);
}
