package study.movie.domain.ticket;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.member.Member;
import study.movie.domain.member.MemberRepository;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
@Transactional
class TicketRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Test
    void 임시테스트_고객이_예약한_티켓을_조회한다() {
        // given
        Member member = Member.builder()
                .name("홍길동")
                .email("honggildong@naver.com")
                .birth(LocalDate.of(1980, 3, 22))
                .build();
        Member savedMember = memberRepository.save(member);

        Ticket ticket = Ticket.builder()
                .member(savedMember)
                .price(7000L)
                .reservedId(UUID.randomUUID().toString())
                .build();
        Ticket reservedTicket = ticketRepository.save(ticket);

        //when
        Ticket findTicket = ticketRepository.findById(reservedTicket.getId()).get();

        //then
        Assertions.assertThat(findTicket).isEqualTo(reservedTicket);
    }
}