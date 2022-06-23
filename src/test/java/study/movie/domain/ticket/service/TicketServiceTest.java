package study.movie.domain.ticket.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.member.entity.Member;
import study.movie.domain.member.repository.MemberRepository;
import study.movie.domain.payment.entity.Payment;
import study.movie.domain.payment.repository.PaymentRepository;
import study.movie.domain.schedule.entity.Schedule;
import study.movie.domain.schedule.repository.ScheduleRepository;
import study.movie.domain.ticket.dto.request.CancelReservationRequest;
import study.movie.domain.ticket.dto.request.ReserveTicketRequest;
import study.movie.domain.ticket.entity.Ticket;
import study.movie.domain.ticket.entity.TicketStatus;
import study.movie.domain.ticket.repository.TicketRepository;
import study.movie.global.dto.IdListRequest;
import study.movie.global.dto.PostIdResponse;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static study.movie.global.utils.NumberUtil.getRandomIndex;

@SpringBootTest
@Transactional
@Slf4j
@Rollback
class TicketServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    TicketServiceImpl ticketService;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PaymentRepository paymentRepository;

    private Schedule initialRandomSchedule;
    private Member initialRandomMember;
    private Payment initialPayment;
    @BeforeEach
    void setUp() {
        initialRandomMember = memberRepository.findAll().get(0);
        initialRandomSchedule = scheduleRepository.findAll()
                .get((int) getRandomIndex(scheduleRepository.count()));

        initialPayment = Payment.basicBuilder()
                .merchantUid("1111")
                .amount(10000)
                .buyerEmail("test@test.com")
                .buyerMobile("010-1111-1111")
                .buyerName("test")
                .build();
        paymentRepository.save(initialPayment);
    }

    @Test
    void 티켓_예매() {
        // given
        List<String> seats = Arrays.asList("A1", "A2");

        // when
        ReserveTicketRequest request = new ReserveTicketRequest();
        request.setMemberEmail(initialRandomMember.getEmail());
        request.setScheduleNumber(initialRandomSchedule.getScheduleNumber());
        request.setMerchantUid(initialPayment.getMerchantUid());
        request.setSeats(seats);

        PostIdResponse response = ticketService.reserve(request);

        Ticket findTicket = ticketRepository.findById(response.getId()).orElseThrow();

        // then
        assertEquals(request.getSeats().size(), findTicket.getReservedMemberCount());
    }

    @Test
    void 티켓_예매_취소(){
        // given
        List<String> seats = Arrays.asList("A1", "A2");

        ReserveTicketRequest request = new ReserveTicketRequest();
        request.setMemberEmail(initialRandomMember.getEmail());
        request.setScheduleNumber(initialRandomSchedule.getScheduleNumber());
        request.setMerchantUid(initialPayment.getMerchantUid());
        request.setSeats(seats);
        PostIdResponse response = ticketService.reserve(request);


        // when
        Ticket savedTicket = ticketRepository.findById(response.getId()).orElseThrow();

        CancelReservationRequest request1 = new CancelReservationRequest();
        request1.setMemberEmail(initialRandomMember.getEmail());
        request1.setReservedNumber(savedTicket.getReserveNumber());

        ticketService.cancelReservation(request1);

        // then
        assertEquals(TicketStatus.CANCEL, savedTicket.getTicketStatus());
    }

    @Test
    void 취소되지_않은_티켓_데이터_삭제(){
        // given
        List<String> seats = Arrays.asList("A2", "C2");

        ReserveTicketRequest request = new ReserveTicketRequest();
        request.setMemberEmail(initialRandomMember.getEmail());
        request.setScheduleNumber(initialRandomSchedule.getScheduleNumber());
        request.setMerchantUid(initialPayment.getMerchantUid());
        request.setSeats(seats);
        PostIdResponse response = ticketService.reserve(request);

        // when
        Ticket savedTicket = ticketRepository.findById(response.getId()).orElseThrow();

        IdListRequest idRequest = new IdListRequest();
        idRequest.setIds(Collections.singletonList(savedTicket.getId()));

        ticketService.delete(idRequest);

        // then
        assertThat(ticketRepository.existsById(savedTicket.getId())).isTrue();
    }

    @Test
    void 취소된_티켓_데이터_삭제(){
        // given
        List<String> seats = Arrays.asList("A2", "C2");

        ReserveTicketRequest request = new ReserveTicketRequest();
        request.setMemberEmail(initialRandomMember.getEmail());
        request.setScheduleNumber(initialRandomSchedule.getScheduleNumber());
        request.setMerchantUid(initialPayment.getMerchantUid());
        request.setSeats(seats);
        PostIdResponse response = ticketService.reserve(request);

        Ticket savedTicket = ticketRepository.findById(response.getId()).orElseThrow();
        Long id = savedTicket.getId();
        IdListRequest idRequest = new IdListRequest();
        idRequest.setIds(Collections.singletonList(id));


        // when
        CancelReservationRequest request1 = new CancelReservationRequest();
        request1.setMemberEmail(initialRandomMember.getEmail());
        request1.setReservedNumber(savedTicket.getReserveNumber());
        ticketService.cancelReservation(request1);

        ticketService.delete(idRequest);

        // then
        assertThat(ticketRepository.existsById(id)).isFalse();
    }
}
