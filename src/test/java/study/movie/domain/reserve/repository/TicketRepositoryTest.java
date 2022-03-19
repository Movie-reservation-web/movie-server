package study.movie.domain.reserve.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.reserve.Ticket;

import java.time.LocalDateTime;

import static study.movie.global.constants.EntityAttrConst.ReserveStatus.RESERVE;

@SpringBootTest
@Transactional
class TicketRepositoryTest {

    @Autowired
    TicketRepository ticketRepository;

    @Test
    public void 티켓_저장_조회() throws Exception {
        // given
        Ticket ticket = Ticket.builder()
                .reserveDate(LocalDateTime.now())
                .reserveNumber("1234-1234-1234-123")
                .reserveStatus(RESERVE)
                .build();
        Ticket savedTicket = ticketRepository.save(ticket);

        // when
        Ticket findTicket = ticketRepository.findById(savedTicket.getId()).get();

        // then
        Assertions.assertEquals(savedTicket, findTicket);
    }


}