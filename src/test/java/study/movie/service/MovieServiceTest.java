package study.movie.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.domain.member.Member;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.Review;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.ScreenTime;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.Screen;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.theater.Theater;
import study.movie.dto.schedule.response.SimpleMovieResponse;
import study.movie.dto.ticket.request.ReserveTicketRequest;
import study.movie.repository.movie.ReviewRepository;
import study.movie.repository.schedule.ScheduleRepository;
import study.movie.service.movie.MovieService;
import study.movie.service.ticket.TicketService;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
@Commit
class MovieServiceTest {
    @Autowired
    EntityManager em;

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    TicketService ticketService;
    @Autowired
    MovieService movieService;

    @Autowired
    InitService init;

    @Test
    void 리뷰_삭제_orphanRemoval_고아객체_삭제() throws Exception {
        // given
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        Review savedReview = init.writeReview(movie);
        Long id = savedReview.getId();
        em.clear();

        // when
        System.out.println("============START DELETE REVIEW=============");

        Review findReview = reviewRepository.findById(id).get();
        findReview.delete();

        em.flush(); // transactional 사용하기 위해
        System.out.println("============END DELETE REVIEW=============");
        // then
    }

    @Test
    void 리뷰_삭제_연관관계_제거_후_삭제() throws Exception {
        // given
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        Review savedReview = init.writeReview(movie);
        Long id = savedReview.getId();
        em.clear();

        // when
        System.out.println("============START DELETE REVIEW=============");

        Review findReview = reviewRepository.findById(id).get();
        findReview.delete();
        reviewRepository.delete(findReview);

        System.out.println("============END DELETE REVIEW=============");

        // then
    }

    @Test
    void 리뷰_삭제_Cascade_PERSIST() throws Exception {
        // given
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        Review savedReview = init.writeReview(movie);
        Long id = savedReview.getId();
        em.clear();

        // when
        System.out.println("============START DELETE REVIEW=============");
        reviewRepository.deleteByIdEqQuery(id);
        System.out.println("============END DELETE REVIEW=============");
        // then
    }

    @Test
    void 상영중인_영화_차트() {
        // given
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie1 = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        Movie movie2 = init.createMovie("영화2", "홍길동", Arrays.asList(FilmFormat.TWO_D));

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

        // when
        int movie1View = 10;
        int movie2View = 20;
        movie1.addAudience(movie1View);
        movie2.addAudience(movie2View);
        em.flush();

        String movie1Rate = String.format("%.1f", (double) movie1View / (double) 30 * 100.0) + "%";
        String movie2Rate = String.format("%.1f", (double) movie2View / (double) 30 * 100.0) + "%";

        List<SimpleMovieResponse> movieCharts = movieService.
                findAllOpenMovies();

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
        Movie movie1 = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        Movie movie2 = init.createMovie("영화2", "홍길동", Arrays.asList(FilmFormat.TWO_D));
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