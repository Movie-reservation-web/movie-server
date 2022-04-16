package study.movie.service.ticket;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.member.GenderType;
import study.movie.domain.member.Member;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.FilmRating;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.MovieGenre;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.ScreenTime;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.Screen;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.theater.Theater;
import study.movie.domain.ticket.Ticket;
import study.movie.domain.ticket.TicketStatus;
import study.movie.dto.ticket.ReserveTicketRequest;
import study.movie.dto.ticket.ReserveTicketResponse;
import study.movie.repository.ticket.TicketRepository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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


    private Member createMember() {
        Member member = Member.builder()
                .name("홍길동")
                .nickname("홍길동")
                .email("aaa@naver.com")
                .gender(GenderType.MALE)
                .birth(LocalDate.now())
                .build();
        em.persist(member);
        em.flush();
        return member;

    }

    private Theater createTheater(String theaterName, CityCode city, String phone) {
        Theater theater = Theater.builder()
                .name(theaterName)
                .city(CityCode.SEL)
                .phone(phone)
                .build();
        em.persist(theater);
        em.flush();
        return theater;
    }

    private Screen registerScreen(String screenName, ScreenFormat format, Theater theater, int maxCols, int maxRows) {
        return Screen.builder()
                .name(screenName)
                .format(format)
                .theater(theater)
                .maxCols(maxCols)
                .maxRows(maxRows)
                .build();
    }

    private Movie createMovie(String title, String director) {
        Movie movie = Movie.builder()
                .title(title)
                .director(director)
                .actors(Arrays.asList("aa", "bb"))
                .formats(Arrays.asList(FilmFormat.TWO_D, FilmFormat.FOUR_D_FLEX))
                .filmRating(FilmRating.G_RATED)
                .genres(Arrays.asList(MovieGenre.values()[0], MovieGenre.values()[1]))
                .image("abc.jpg")
                .info("information")
                .nation("korea")
                .runningTime(160)
                .releaseDate(LocalDate.now())
                .build();
        em.persist(movie);
        em.flush();
        return movie;
    }

    @Test
    public void 티켓_예매() throws Exception {
        // given
        Member member = createMember();
        Theater theater = createTheater("CGV 용산", CityCode.SEL, "000-000");
        Screen screen = registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = createMovie("영화1", "홍길동");
        LocalDate screenDate = LocalDate.of(2022, 3, 10);
        ScreenTime screenTime = new ScreenTime(screenDate.atTime(3, 2, 21), movie.getRunningTime());

        Schedule savedSchedule = Schedule.builder()
                .screenTime(screenTime)
                .screen(screen)
                .movie(movie)
                .build();
        em.flush();
        List<String> seats = Arrays.asList("A2", "C2");

        // when
        ReserveTicketRequest request = new ReserveTicketRequest();
        request.setMemberId(member.getId());
        request.setScheduleId(savedSchedule.getId());
        request.setSeats(seats);

        ReserveTicketResponse response = ticketService.reserve(request);

        Ticket findTicket = ticketRepository.findByReserveNumber(response.getReserveNumber()).orElseThrow();

        // then
        assertEquals(response.getReservedMemberCount(), findTicket.getReservedMemberCount());
        assertEquals(response.getReserveNumber(), findTicket.getReserveNumber());
    }

    @Test
    public void 티켓_예매_취소() throws Exception {
        // given
        Member member = createMember();
        Theater theater = createTheater("CGV 용산", CityCode.SEL, "000-000");
        Screen screen = registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = createMovie("영화1", "홍길동");
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
        ReserveTicketResponse response = ticketService.reserve(request);

        Ticket savedTicket = ticketRepository.findByReserveNumber(response.getReserveNumber()).orElseThrow();

        // when
        ticketService.cancelReservation(savedTicket.getReserveNumber());

        // then
        assertEquals(TicketStatus.CANCEL, savedTicket.getTicketStatus());
    }
}