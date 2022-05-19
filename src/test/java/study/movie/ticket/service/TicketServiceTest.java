package study.movie.ticket.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.global.dto.IdListRequest;
import study.movie.global.dto.PostIdResponse;
import study.movie.exception.CustomException;
import study.movie.member.entity.Member;
import study.movie.movie.entity.Movie;
import study.movie.schedule.entity.Schedule;
import study.movie.schedule.entity.ScreenTime;
import study.movie.schedule.repository.ScheduleRepository;
import study.movie.theater.entity.CityCode;
import study.movie.theater.entity.Screen;
import study.movie.theater.entity.ScreenFormat;
import study.movie.theater.entity.Theater;
import study.movie.ticket.dto.request.ReserveTicketRequest;
import study.movie.ticket.entity.Ticket;
import study.movie.ticket.entity.TicketStatus;
import study.movie.ticket.repository.TicketRepository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
@Slf4j
//@Rollback
@Commit
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
    InitService init;


    @Test
    public void 티켓_예매() throws Exception {
        // given
        Member member = init.createMember();
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createBasicMovie();
        LocalDate screenDate = LocalDate.of(2022, 3, 10);
        ScreenTime screenTime = new ScreenTime(screenDate.atTime(3, 2, 21), movie.getRunningTime());
        Schedule schedule = Schedule.builder()
                .screenTime(screenTime)
                .screen(screen)
                .movie(movie)
                .build();
        scheduleRepository.save(schedule);
        List<String> seats = Arrays.asList("A2", "C2");

        // when
        ReserveTicketRequest request = new ReserveTicketRequest();
        request.setMemberId(member.getId());
        request.setScheduleId(schedule.getId());
        request.setSeats(seats);

        PostIdResponse response = ticketService.reserve(request);

        Ticket findTicket = ticketRepository.findById(response.getId()).orElseThrow();
        log.info("findTicket={}", findTicket);
        // then
        assertEquals(request.getSeats().size(), findTicket.getReservedMemberCount());
    }

    @Test
    public void 티켓_예매_취소() throws Exception {
        // given
        Member member = init.createMember();
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createBasicMovie();
        LocalDate screenDate = LocalDate.of(2022, 3, 10);
        ScreenTime screenTime = new ScreenTime(screenDate.atTime(3, 2, 21), movie.getRunningTime());

        Schedule savedSchedule = Schedule.builder()
                .screenTime(screenTime)
                .screen(screen)
                .movie(movie)
                .build();
        em.flush();
        List<String> seats = Arrays.asList("A2", "C2");

        ReserveTicketRequest request = new ReserveTicketRequest();
        request.setMemberId(member.getId());
        request.setScheduleId(savedSchedule.getId());
        request.setSeats(seats);
        PostIdResponse response = ticketService.reserve(request);

        Ticket savedTicket = ticketRepository.findById(response.getId()).orElseThrow();

        // when
        ticketService.cancelReservation(savedTicket.getReserveNumber(), member.getId());

        boolean isTicketInMovie = savedTicket.getMovie().getTickets().contains(savedTicket);
        boolean isTicketInMember = savedTicket.getMember().getTickets().contains(savedTicket);
        // then
        assertEquals(TicketStatus.CANCEL, savedTicket.getTicketStatus());
        assertFalse(isTicketInMovie);
        assertFalse(isTicketInMember);
    }

    @Test
    public void 취소되지_않은_티켓_데이터_삭제() throws Exception {
        // given
        Member member = init.createMember();
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createBasicMovie();
        LocalDate screenDate = LocalDate.of(2022, 3, 10);
        ScreenTime screenTime = new ScreenTime(screenDate.atTime(3, 2, 21), movie.getRunningTime());

        Schedule savedSchedule = Schedule.builder()
                .screenTime(screenTime)
                .screen(screen)
                .movie(movie)
                .build();
        em.flush();
        List<String> seats = Arrays.asList("A2", "C2");

        ReserveTicketRequest request = new ReserveTicketRequest();
        request.setMemberId(member.getId());
        request.setScheduleId(savedSchedule.getId());
        request.setSeats(seats);
        PostIdResponse response = ticketService.reserve(request);

        Ticket savedTicket = ticketRepository.findById(response.getId()).orElseThrow();
        IdListRequest idRequest = new IdListRequest();
        idRequest.setIds(Collections.singletonList(savedTicket.getId()));

        // then
        assertThatThrownBy(() -> ticketService.delete(idRequest))
                .isInstanceOf(CustomException.class);
    }

    @Test
    public void 취소된_티켓_데이터_삭제() throws Exception {
        // given
        Member member = init.createMember();
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createBasicMovie();
        LocalDate screenDate = LocalDate.of(2022, 3, 10);
        ScreenTime screenTime = new ScreenTime(screenDate.atTime(3, 2, 21), movie.getRunningTime());

        Schedule savedSchedule = Schedule.builder()
                .screenTime(screenTime)
                .screen(screen)
                .movie(movie)
                .build();
        em.flush();
        List<String> seats = Arrays.asList("A2", "C2");

        ReserveTicketRequest request = new ReserveTicketRequest();
        request.setMemberId(member.getId());
        request.setScheduleId(savedSchedule.getId());
        request.setSeats(seats);
        PostIdResponse response = ticketService.reserve(request);

        Ticket savedTicket = ticketRepository.findById(response.getId()).orElseThrow();
        Long id = savedTicket.getId();
        IdListRequest idRequest = new IdListRequest();
        idRequest.setIds(Collections.singletonList(id));

        // when
        ticketService.cancelReservation(savedTicket.getReserveNumber(), member.getId());

        ticketService.delete(idRequest);

        // then
        assertThatThrownBy(() -> ticketRepository.findById(id).orElseThrow())
                .isInstanceOf(NoSuchElementException.class);
    }
}