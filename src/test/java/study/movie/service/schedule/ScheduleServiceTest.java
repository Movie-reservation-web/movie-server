package study.movie.service.schedule;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.Review;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.ScreenTime;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.Screen;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.theater.Theater;
import study.movie.dto.schedule.condition.ScheduleBasicSearchCond;
import study.movie.dto.schedule.request.CreateScheduleRequest;
import study.movie.dto.schedule.request.ScheduleScreenRequest;
import study.movie.dto.schedule.response.*;
import study.movie.repository.movie.ReviewRepository;
import study.movie.repository.schedule.ScheduleRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static study.movie.domain.theater.ScreenFormat.*;

@SpringBootTest
@Transactional
@Slf4j
@Commit
class ScheduleServiceTest {
    @Autowired
    EntityManager em;

    @Autowired
    ScheduleServiceImpl scheduleService;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    InitService init;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    EntityManagerFactory emf;
    @Autowired
    JPAQueryFactory queryFactory;

    @Test
    public void 상영일정_저장() throws Exception {
        // given
        Theater theater = init.createTheater("용산 CGV", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        LocalDateTime startTime = LocalDateTime.now().plusDays(10);
        em.flush();
        // when
        CreateScheduleRequest request = new CreateScheduleRequest(startTime, movie.getId(), screen.getId());
        CreateScheduleResponse response = scheduleService.save(request);

        // then
        assertEquals(theater.getName(), response.getTheaterName());
        assertEquals(movie.getTitle(), response.getMovieTitle());
        assertEquals(startTime, response.getScreenTime().getStartDateTime());
    }

    @Test
    public void 상영일정_조건_검색_중복된_값_제거() throws Exception {
        // given
        String theaterName = "CGV 용산";
        Theater theater = init.createTheater(theaterName, CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        LocalDate screenDate = LocalDate.now().plusDays(10);
        ScreenTime screenTime1 = new ScreenTime(screenDate.atTime(3, 2, 21), movie.getRunningTime());
        ScreenTime screenTime2 = new ScreenTime(screenDate.atTime(6, 2, 21), movie.getRunningTime());
        Schedule savedSchedule1 = Schedule.builder()
                .screenTime(screenTime1)
                .screen(screen)
                .movie(movie)
                .build();

        Schedule savedSchedule2 = Schedule.builder()
                .screenTime(screenTime2)
                .screen(screen)
                .movie(movie)
                .build();

        ScheduleBasicSearchCond cond = new ScheduleBasicSearchCond();
        cond.setTheaterName(theaterName);

        // when
        List<ScheduleSearchResponse> scheduleSearchResponse = scheduleService.searchBasicSchedules(cond);
        List<Schedule> schedules = scheduleRepository.searchBasicSchedules(cond);
        long dbCount = schedules.size();

        // then
        assertThat(schedules).containsExactly(savedSchedule1, savedSchedule2);
        assertEquals(dbCount - 1, scheduleSearchResponse.size(), "시간은 다르지만 날짜가 같은 객체는 중복제거되어야 한다.");
        assertEquals(screenDate, scheduleSearchResponse.get(0).getScreenDate());
    }

    @Test
    public void 상영일정_조건_검색_기본_정보() throws Exception {
        // given
        String theaterName1 = "CGV 용산";
        String theaterName2 = "CGV 강남";
        List<Screen> screens1 = init.addTheaterScreen(
                theaterName1,
                CityCode.SEL,
                Arrays.asList("1관", "2관", "3관", "4관", "5관"),
                Arrays.asList(TWO_D, IMAX, FOUR_D_FLEX, SCREEN_X, PREMIUM));
        List<Screen> screens2 = init.addTheaterScreen(
                theaterName2,
                CityCode.SEL,
                Arrays.asList("1관", "2관", "3관", "4관"),
                Arrays.asList(TWO_D, TWO_D, FOUR_D_FLEX, PREMIUM));

        List<Movie> movies = init.addMovies(
                Arrays.asList("영화1", "영화2", "영화3", "영화4", "영화5", "영화6"),
                Arrays.asList("감독1", "감독2", "감독3", "감독4", "감독5", "감독6"),
                Arrays.asList(
                        Arrays.asList(FilmFormat.TWO_D, FilmFormat.FOUR_D_FLEX, FilmFormat.IMAX),
                        Arrays.asList(FilmFormat.TWO_D, FilmFormat.SCREEN_X),
                        Arrays.asList(FilmFormat.TWO_D, FilmFormat.FOUR_D_FLEX, FilmFormat.IMAX, FilmFormat.SCREEN_X),
                        Arrays.asList(FilmFormat.TWO_D),
                        Arrays.asList(FilmFormat.TWO_D, FilmFormat.IMAX),
                        Arrays.asList(FilmFormat.TWO_D, FilmFormat.FOUR_D_FLEX)
                )
        );
        LocalDate screenDate = LocalDate.now().plusDays(10);
        Movie savedMovie = movies.get(0);
        ScreenTime screenTime = new ScreenTime(screenDate.atTime(3, 2, 21), savedMovie.getRunningTime());
        // CGV 용산, 상영관1, 영화1, 2D
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens1.get(0))
                .movie(savedMovie)
                .build();
        // CGV 강남, 상영관2, 영화1, 2D
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens2.get(1))
                .movie(savedMovie)
                .build();
        // CGV 용산, 상영관2, 영화1, IMAX
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens1.get(1))
                .movie(savedMovie)
                .build();
        // CGV 강남, 상영관3, 영화2, 4DX
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens2.get(2))
                .movie(movies.get(3))
                .build();

        ScreenFormat screenFormat = ScreenFormat.TWO_D;
        // 검색 조건 -> 영화1(2D)
        ScheduleBasicSearchCond cond = new ScheduleBasicSearchCond();
        cond.setFormat(screenFormat);
        cond.setMovieTitle(savedMovie.getTitle());

        // when
        List<ScheduleSearchResponse> searchResponses = scheduleService.searchBasicSchedules(cond);

        // then
        for (ScheduleSearchResponse searchResponse : searchResponses) {
            assertEquals(savedMovie.getTitle(), searchResponse.getMovie().getMovieTitle());
            assertEquals(savedMovie.getImage(), searchResponse.getMovie().getImage());
            assertEquals(savedMovie.getFilmRating().getValue(), searchResponse.getMovie().getFilmRating());
        }
    }

    @Test
    public void 상영일정_조건_검색_상영관_정보() throws Exception {
        // given
        String theaterName1 = "CGV 용산";
        String theaterName2 = "CGV 강남";
        List<Screen> screens1 = init.addTheaterScreen(
                theaterName1,
                CityCode.SEL,
                Arrays.asList("1관", "2관", "3관", "4관", "5관"),
                Arrays.asList(TWO_D, IMAX, FOUR_D_FLEX, SCREEN_X, PREMIUM));
        List<Screen> screens2 = init.addTheaterScreen(
                theaterName2,
                CityCode.SEL,
                Arrays.asList("1관", "2관", "3관", "4관"),
                Arrays.asList(TWO_D, TWO_D, FOUR_D_FLEX, PREMIUM));

        List<Movie> movies = init.addMovies(
                Arrays.asList("영화1", "영화2", "영화3", "영화4", "영화5", "영화6"),
                Arrays.asList("감독1", "감독2", "감독3", "감독4", "감독5", "감독6"),
                Arrays.asList(
                        Arrays.asList(FilmFormat.TWO_D, FilmFormat.FOUR_D_FLEX, FilmFormat.IMAX),
                        Arrays.asList(FilmFormat.TWO_D, FilmFormat.SCREEN_X),
                        Arrays.asList(FilmFormat.TWO_D, FilmFormat.FOUR_D_FLEX, FilmFormat.IMAX, FilmFormat.SCREEN_X),
                        Arrays.asList(FilmFormat.TWO_D),
                        Arrays.asList(FilmFormat.TWO_D, FilmFormat.IMAX),
                        Arrays.asList(FilmFormat.TWO_D, FilmFormat.FOUR_D_FLEX)
                )
        );
        LocalDate screenDate = LocalDate.now().plusDays(10);
        Movie savedMovie = movies.get(0);
        ScreenTime screenTime = new ScreenTime(screenDate.atTime(3, 2, 21), savedMovie.getRunningTime());
        // CGV 용산, 상영관1, 영화1, 2D
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens1.get(0))
                .movie(savedMovie)
                .build();
        // CGV 강남, 상영관2, 영화1, 2D
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens2.get(1))
                .movie(savedMovie)
                .build();
        // CGV 용산, 상영관2, 영화1, IMAX
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens1.get(1))
                .movie(savedMovie)
                .build();
        // CGV 강남, 상영관3, 영화2, 4DX
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens2.get(2))
                .movie(movies.get(3))
                .build();

        ScreenFormat screenFormat = ScreenFormat.TWO_D;
        // 검색 조건 -> 영화1(전체), CGV 용산, 상영날짜 22/03/10
        ScheduleScreenRequest request = new ScheduleScreenRequest();
        request.setTheaterName(theaterName1);
        request.setMovieTitle(savedMovie.getTitle());
        request.setScreenDate(screenDate);
        request.setScreenFormat(screenFormat);

        // when
        List<ScheduleScreenResponse> screenResponses = scheduleService.searchScheduleScreens(request);

        // then
        for (ScheduleScreenResponse screenResponse : screenResponses) {
            assertEquals(screenTime, screenResponse.getScreenTime());
        }
    }

    @Test
    public void 상영일정_조회_영화_포멧() throws Exception {
        String theaterName1 = "CGV 용산";
        String theaterName2 = "CGV 강남";
        List<Screen> screens1 = init.addTheaterScreen(
                theaterName1,
                CityCode.SEL,
                Arrays.asList("1관", "2관", "3관", "4관", "5관"),
                Arrays.asList(TWO_D, IMAX, FOUR_D_FLEX, SCREEN_X, PREMIUM));
        List<Screen> screens2 = init.addTheaterScreen(
                theaterName2,
                CityCode.SEL,
                Arrays.asList("1관", "2관", "3관", "4관"),
                Arrays.asList(TWO_D, TWO_D, FOUR_D_FLEX, PREMIUM));

        List<Movie> movies = init.addMovies(
                Arrays.asList("영화1", "영화2", "영화3", "영화4", "영화5", "영화6"),
                Arrays.asList("감독1", "감독2", "감독3", "감독4", "감독5", "감독6"),
                Arrays.asList(
                        Arrays.asList(FilmFormat.TWO_D, FilmFormat.FOUR_D_FLEX, FilmFormat.IMAX),
                        Arrays.asList(FilmFormat.TWO_D, FilmFormat.SCREEN_X),
                        Arrays.asList(FilmFormat.TWO_D, FilmFormat.FOUR_D_FLEX, FilmFormat.IMAX, FilmFormat.SCREEN_X),
                        Arrays.asList(FilmFormat.TWO_D),
                        Arrays.asList(FilmFormat.TWO_D, FilmFormat.IMAX),
                        Arrays.asList(FilmFormat.TWO_D, FilmFormat.FOUR_D_FLEX)
                )
        );
        LocalDate screenDate = LocalDate.now().plusDays(10);
        Movie savedMovie = movies.get(0);
        ScreenTime screenTime = new ScreenTime(screenDate.atTime(3, 2, 21), savedMovie.getRunningTime());
        // CGV 용산, 상영관1, 영화1, 2D
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens1.get(0))
                .movie(savedMovie)
                .build();
        // CGV 강남, 상영관2, 영화1, 2D
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens2.get(1))
                .movie(savedMovie)
                .build();
        // CGV 용산, 상영관2, 영화1, IMAX
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens1.get(1))
                .movie(savedMovie)
                .build();
        // CGV 강남, 상영관3, 영화2, 4DX
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens2.get(2))
                .movie(movies.get(3))
                .build();

        // when
        // 검색 조건 -> 영화 1
        MovieFormatResponse response = scheduleService.searchScheduleByMovie(savedMovie.getTitle());

        // then
        assertEquals(savedMovie.getTitle(), response.getMovieTitle());
        assertThat(response.getFormats()).containsExactly(TWO_D, IMAX);
    }

    @Test
    public void 모든_상영일정_조회() throws Exception {
        // given
        String theaterName = "CGV 용산";
        Theater theater = init.createTheater(theaterName, CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        LocalDate screenDate = LocalDate.now().plusDays(10);
        ScreenTime screenTime1 = new ScreenTime(screenDate.atTime(3, 2, 21), movie.getRunningTime());
        ScreenTime screenTime2 = new ScreenTime(screenDate.atTime(6, 2, 21), movie.getRunningTime());

        // when
        Schedule.builder()
                .screenTime(screenTime1)
                .screen(screen)
                .movie(movie)
                .build();
        Schedule s1 = Schedule.builder()
                .screenTime(screenTime2)
                .screen(screen)
                .movie(movie)
                .build();
        em.flush();
        em.clear();
        List<ScheduleSearchResponse> schedules = scheduleService.findAllSchedules();

        // then
        assertEquals(2, schedules.size());
        for (ScheduleSearchResponse schedule : schedules) {
            assertEquals(schedule.getMovie().getMovieTitle(), movie.getTitle());
            assertEquals(schedule.getTheater().getTheaterName(), theater.getName());
        }
    }

    @Test
    public void 상영일정_삭제() throws Exception {
        // given
        Theater theater = init.createTheater("용산 CGV", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        ScreenTime screenTime = new ScreenTime(LocalDateTime.now().plusDays(10), movie.getRunningTime());

        Schedule savedSchedule = Schedule.builder()
                .screenTime(screenTime)
                .screen(screen)
                .movie(movie)
                .build();
        em.flush();
        em.clear();

        // when
        long count = scheduleRepository.count();
        scheduleService.removeSchedule(Collections.singletonList(savedSchedule.getId()));
        // then
        assertEquals(count - 1, scheduleRepository.count());
        assertFalse(em.find(Screen.class, screen.getId()).getSchedules().contains(savedSchedule));
        assertFalse(em.find(Movie.class, movie.getId()).getSchedules().contains(savedSchedule));
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

        List<SimpleMovieResponse> movieCharts = scheduleService.findAllOpenMovies();

        // then
        log.info("movieCharts={}", movieCharts);
        assertThat(movieCharts.size()).isEqualTo(2);
        assertThat(movieCharts.get(0).getId()).isEqualTo(movie2.getId());
        assertThat(movieCharts.get(0).getReservationRate()).isEqualTo(movie2Rate);
        assertThat(movieCharts.get(1).getId()).isEqualTo(movie1.getId());
        assertThat(movieCharts.get(1).getReservationRate()).isEqualTo(movie1Rate);
    }


    @Test
    public void 리뷰삭제() throws Exception {
        // given
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        Review review = Review.builder()
                .comment("리뷰")
                .movie(movie)
                .score(12F)
                .writer("asdasd")
                .build();

        em.flush();

        // when
        review.delete();
        reviewRepository.deleteById(review.getId());

        // then
        assertThat(reviewRepository.count()).isEqualTo(0);
    }


}