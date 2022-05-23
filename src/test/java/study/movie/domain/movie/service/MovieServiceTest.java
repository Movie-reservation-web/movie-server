package study.movie.domain.movie.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.domain.member.entity.Member;
import study.movie.domain.schedule.entity.Schedule;
import study.movie.domain.schedule.entity.ScreenTime;
import study.movie.domain.theater.entity.CityCode;
import study.movie.domain.theater.entity.Screen;
import study.movie.domain.theater.entity.ScreenFormat;
import study.movie.domain.theater.entity.Theater;
import study.movie.domain.movie.dto.condition.MovieChartSortType;
import study.movie.domain.schedule.dto.response.MovieChartResponse;
import study.movie.domain.ticket.dto.request.ReserveTicketRequest;
import study.movie.domain.movie.entity.FilmFormat;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.movie.repository.MovieRepository;
import study.movie.domain.movie.repository.ReviewRepository;
import study.movie.domain.schedule.repository.ScheduleRepository;
import study.movie.domain.ticket.service.TicketService;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class MovieServiceTest {
    @Autowired
    EntityManager em;

    @Autowired
    MovieService movieService;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    TicketService ticketService;

    @Autowired
    InitService init;

    @Test
    void 상영중인_영화_차트() {
        // given
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie1 = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D), Arrays.asList("배우1"));
        Movie movie2 = init.createMovie("영화2", "홍길동", Arrays.asList(FilmFormat.TWO_D), Arrays.asList("배우2"));

        ScreenTime screenTime = new ScreenTime(LocalDateTime.now().plusDays(5), movie1.getRunningTime());
        ScreenTime screenTime1 = new ScreenTime(LocalDateTime.now().plusDays(4), movie1.getRunningTime());
        Schedule savedSchedule = Schedule.builder()
                .screenTime(screenTime)
                .screen(screen)
                .movie(movie1)
                .build();
        Schedule savedSchedule2 = Schedule.builder()
                .screenTime(screenTime1)
                .screen(screen)
                .movie(movie2)
                .build();
        scheduleRepository.save(savedSchedule);
        scheduleRepository.save(savedSchedule2);

        // when
        int movie1View = 10;
        int movie2View = 20;
        movie1.addAudience(movie1View);
        movie2.addAudience(movie2View);
        em.flush();
        em.clear();
        String movie1Rate = String.format("%.1f", (double) movie1View / (double) 30 * 100.0) + "%";
        String movie2Rate = String.format("%.1f", (double) movie2View / (double) 30 * 100.0) + "%";

        List<MovieChartResponse> movieCharts = movieService.findMovieBySort(MovieChartSortType.AUDIENCE_DESC, false);

        // then
        log.info("movieCharts={}", movieCharts);
        assertThat(movieCharts.size()).isEqualTo(2);
        assertThat(movieCharts.get(0).getId()).isEqualTo(movie2.getId());
        assertThat(movieCharts.get(0).getReservationRate()).isEqualTo(movie2Rate);
        assertThat(movieCharts.get(1).getId()).isEqualTo(movie1.getId());
        assertThat(movieCharts.get(1).getReservationRate()).isEqualTo(movie1Rate);
    }

    @Test
    void 영화_관람객_수_증가() {
        // given
        Member member = init.createMember();
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie1 = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D), Arrays.asList("배우1"));
        Movie movie2 = init.createMovie("영화2", "홍길동", Arrays.asList(FilmFormat.TWO_D), Arrays.asList("배우1"));
        LocalDate screenDate = LocalDate.of(2022, 3, 10);
        ScreenTime screenTime1 = new ScreenTime(screenDate.atTime(3, 2, 21), movie1.getRunningTime());
        ScreenTime screenTime2 = new ScreenTime(screenDate.atTime(10, 2, 21), movie1.getRunningTime());
        Schedule schedule1 = Schedule.builder()
                .screenTime(screenTime1)
                .screen(screen)
                .movie(movie1)
                .build();
        Schedule schedule2 = Schedule.builder()
                .screenTime(screenTime2)
                .screen(screen)
                .movie(movie2)
                .build();
        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);

        ReserveTicketRequest request1 = new ReserveTicketRequest();
        request1.setMemberId(member.getId());
        request1.setScheduleId(schedule1.getId());
        request1.setSeats(Arrays.asList("A2", "C2"));
        ReserveTicketRequest request2 = new ReserveTicketRequest();
        request2.setMemberId(member.getId());
        request2.setScheduleId(schedule1.getId());
        request2.setSeats(Arrays.asList("A2", "C2", "B1"));

        ReserveTicketRequest request3 = new ReserveTicketRequest();
        request3.setMemberId(member.getId());
        request3.setScheduleId(schedule2.getId());
        request3.setSeats(Arrays.asList("A2", "C2", "B1", "A1"));

        ticketService.reserve(request1);
        ticketService.reserve(request2);
        ticketService.reserve(request3);

        // when
        movieService.updateMovieAudience();

        // then
        assertThat(movie1.getAudience()).isEqualTo(5);
        assertThat(movie2.getAudience()).isEqualTo(4);
    }
}
