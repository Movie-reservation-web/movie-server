package study.movie.domain.ticket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.member.Member;
import study.movie.repository.member.MemberRepository;
import study.movie.domain.movie.Movie;
import study.movie.repository.movie.MovieRepository;
import study.movie.repository.ticket.TicketRepository;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.theater.Seat;
import study.movie.repository.schedule.ScheduleRepository;
import study.movie.domain.theater.Screen;
import study.movie.repository.theater.ScreenRepository;
import study.movie.global.constants.EntityAttrConst;
import study.movie.global.constants.EntityAttrConst.ReserveStatus;
import study.movie.global.constants.EntityAttrConst.ScreenFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static study.movie.global.constants.EntityAttrConst.FilmFormat.FOUR_D_FLEX;
import static study.movie.global.constants.EntityAttrConst.FilmRating.G_RATED;
import static study.movie.global.constants.EntityAttrConst.MovieGenre.*;
import static study.movie.global.constants.EntityAttrConst.ScreenFormat.GOLD_CLASS;
import static study.movie.global.constants.EntityAttrConst.ScreenFormat.SCREEN_X;
import static study.movie.global.constants.EntityAttrConst.SeatStatus.*;

@SpringBootTest
@Transactional
public class TicketTest {

    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    ScreenRepository screenRepository;
    @Autowired
    MovieRepository movieRepository;

    Member findMember;
    Schedule findSchedule;

    @BeforeEach
    public void setUp() {
        // 회원 정보 저장
        Member member = Member.builder()
                .name("이규연")
                .birth(LocalDate.now())
                .gender(EntityAttrConst.GenderType.MALE)
                .email("rbdus7174@naver.com")
                .nickname("kxuxeon")
                .build();

        Member savedMember = memberRepository.save(member);
        findMember = memberRepository.findById(savedMember.getId()).get();


        // 상영 일정
        List<String> actors = Arrays.asList("actor1", "actor2", "actor3", "actor4", "actor5");
        List<EntityAttrConst.FilmFormat> formats = Arrays.asList(FOUR_D_FLEX, EntityAttrConst.FilmFormat.FOUR_D_FLEX_SCREEN);
        List<EntityAttrConst.MovieGenre> genres = Arrays.asList(DRAMA, ACTION, COMEDY);
        Movie movie = Movie.builder()
                .title("title")
                .director("director")
                .actors(actors)
                .genres(genres)
                .filmRating(G_RATED)
                .runningTime(160)
                .nation("KOREA")
                .releaseDate(LocalDate.of(2022, 3, 1))
                .formats(formats)
                .info("movie information")
                .image("image1.jpg")
                .build();
        Movie savedMovie = movieRepository.save(movie);
        Movie findMovie = movieRepository.findById(savedMovie.getId()).get();

        List<ScreenFormat> screenFormats = Arrays.asList(ScreenFormat.FOUR_D_FLEX_SCREEN, SCREEN_X, GOLD_CLASS);
        List<Seat> seats = new ArrayList<>();
        IntStream.range(0,10).forEach(i -> seats.add(Seat.createSeat('A', i)));

        Screen screen = Screen.builder()
                .name("1관")
                .formats(screenFormats)
                .seats(seats)
                .build();


        Screen savedScreen = screenRepository.save(screen);
        Screen findScreen = screenRepository.findById(savedScreen.getId()).get();

        Schedule schedule = Schedule.createSchedule(findMovie, findScreen, LocalDateTime.now());
        Schedule savedSchedule = scheduleRepository.save(schedule);
        findSchedule = scheduleRepository.findById(savedSchedule.getId()).get();
    }

    @Test
    public void 영화_예매_티켓_생성() throws Exception {
        // given
        Seat seat = Seat.createSeat('A', 5);
        Ticket ticket = Ticket.createTicket(findMember, findSchedule, seat, "1234-1234-1234-123", LocalDateTime.now());
        Ticket savedTicket = ticketRepository.save(ticket);

        // when
        Ticket findTicket = ticketRepository.findById(savedTicket.getId()).get();

        // then
        Assertions.assertEquals(savedTicket, findTicket);
        Assertions.assertTrue(
                findTicket.getSchedule().getScreen()
                        .getSeats(RESERVING).stream()
                        .anyMatch(v -> v.seatToString().equals(seat.seatToString()))
        );
        Assertions.assertEquals(findTicket.getSchedule(), findSchedule);
    }

    @Test
    public void 예매_취소() throws Exception {
        // given
        Seat seat = Seat.createSeat('A', 5);
        Ticket ticket = Ticket.createTicket(findMember, findSchedule, seat, "1234-1234-1234-123", LocalDateTime.now());
        Ticket savedTicket = ticketRepository.save(ticket);
        Ticket findTicket = ticketRepository.findById(savedTicket.getId()).get();

        // when
        findTicket.cancelReserve();

        // then
        Assertions.assertEquals(findTicket.getReserveStatus(), ReserveStatus.CANCEL);
        Assertions.assertFalse(findMember.getTickets().contains(findTicket));
        Assertions.assertFalse(findSchedule.getTickets().contains(findTicket));
    }


}
