package study.movie.domain.ticket;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.member.Member;
import study.movie.domain.member.MemberRepository;
import study.movie.domain.screening.Screening;
import study.movie.domain.screening.ScreeningRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest
@Transactional
class TicketRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    ScreeningRepository screeningRepository;

    @Test
    void 고객이_예약한_티켓을_조회한다() {
        // given
        Screening screening = Screening.builder()
                .startTime(LocalDateTime.now())
                .build();
        Screening savedScreening = screeningRepository.save(screening);

        Member member = Member.builder()
                .name("홍길동")
                .email("honggildong@naver.com")
                .birth(LocalDate.of(1980, 3, 22))
                .build();
        Member savedMember = memberRepository.save(member);

        Ticket ticket = Ticket.builder()
                .member(savedMember)
                .price(7000L)
                .screening(savedScreening)
                .reservedId(UUID.randomUUID().toString())
                .build();
        Ticket reservedTicket = ticketRepository.save(ticket);

        //when
        Ticket findTicket = ticketRepository.findById(reservedTicket.getId()).get();

        //then
        Assertions.assertThat(findTicket).isEqualTo(reservedTicket);
    }
}