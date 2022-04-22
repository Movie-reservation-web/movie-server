package study.movie.service.schedule;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
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
import study.movie.dto.schedule.condition.ScheduleSearchCond;
import study.movie.dto.schedule.request.CreateScheduleRequest;
import study.movie.dto.schedule.response.CreateScheduleResponse;
import study.movie.dto.schedule.response.MovieFormatResponse;
import study.movie.dto.schedule.response.ScheduleScreenResponse;
import study.movie.dto.schedule.response.ScheduleSearchResponse;
import study.movie.repository.movie.ReviewRepository;
import study.movie.repository.schedule.ScheduleRepository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static study.movie.domain.theater.ScreenFormat.*;
import static study.movie.domain.theater.ScreenFormat.PREMIUM;

@SpringBootTest
@Transactional
@Slf4j
@Rollback
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
    @Test
    public void 상영일정_저장() throws Exception {
        // given
        Theater theater = init.createTheater("용산 CGV", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        LocalDateTime startTime = LocalDateTime.of(2022, 5, 10, 3, 2, 21);

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
        LocalDate screenDate = LocalDate.of(2022, 3, 10);
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

        ScheduleSearchCond cond = new ScheduleSearchCond();
        cond.setTheaterName(theaterName);
        cond.setFinalSearch(false);

        // when
        List<ScheduleSearchResponse> scheduleSearchResponse = (List<ScheduleSearchResponse>) scheduleService.searchSchedules(cond);
        List<Schedule> schedules = scheduleRepository.searchSchedules(cond);
        long dbCount = schedules.size();

        // then
        assertThat(schedules).containsExactly(savedSchedule1, savedSchedule2);
        assertEquals(dbCount - 1, scheduleSearchResponse.size(), "시간은 다르지만 날짜가 같은 객체는 중복제거되어야 한다.");
        assertEquals(screenDate, scheduleSearchResponse.get(0).getScreenDate());
    }

    @Test
    public void 상영일정_조건_검색() throws Exception {
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
        LocalDate screenDate = LocalDate.of(2022, 3, 10);
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
        ScheduleSearchCond cond1 = new ScheduleSearchCond();
        cond1.setTheaterName(theaterName1);
        cond1.setMovieTitle(savedMovie.getTitle());
        cond1.setScreenDate(screenDate);
        cond1.setFinalSearch(true);

        // 검색 조건 -> 영화1(2D)
        ScheduleSearchCond cond2 = new ScheduleSearchCond();
        cond2.setFormat(screenFormat);
        cond2.setMovieTitle(savedMovie.getTitle());
        cond2.setFinalSearch(false);

        // when
        List<ScheduleScreenResponse> screenResponses = (List<ScheduleScreenResponse>) scheduleService.searchSchedules(cond1);
        List<ScheduleSearchResponse> searchResponses = (List<ScheduleSearchResponse>) scheduleService.searchSchedules(cond2);

        // then
        for (ScheduleScreenResponse screenResponse : screenResponses) {
            assertEquals(screenTime, screenResponse.getScreenTime());
        }
        for (ScheduleSearchResponse searchResponse : searchResponses) {
            assertEquals(savedMovie.getTitle(), searchResponse.getMovie().getMovieTitle());
            assertEquals(savedMovie.getImage(), searchResponse.getMovie().getImage());
            assertEquals(savedMovie.getFilmRating().getValue(), searchResponse.getMovie().getFilmRating());
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
        LocalDate screenDate = LocalDate.of(2022, 3, 10);
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
        LocalDate screenDate = LocalDate.of(2022, 3, 10);
        ScreenTime screenTime1 = new ScreenTime(screenDate.atTime(3, 2, 21), movie.getRunningTime());
        ScreenTime screenTime2 = new ScreenTime(screenDate.atTime(6, 2, 21), movie.getRunningTime());

        // when
        Schedule.builder()
                .screenTime(screenTime1)
                .screen(screen)
                .movie(movie)
                .build();
        Schedule.builder()
                .screenTime(screenTime2)
                .screen(screen)
                .movie(movie)
                .build();

        List<ScheduleSearchResponse> schedules = scheduleService.findAllSchedules();

        // then
        assertEquals(2, schedules.size());
    }

    @Test
    public void 상영일정_삭제() throws Exception {
        // given
        Theater theater = init.createTheater("용산 CGV", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        ScreenTime screenTime = new ScreenTime(LocalDateTime.of(2022, 5, 10, 3, 2, 21), movie.getRunningTime());

        Schedule savedSchedule = Schedule.builder()
                .screenTime(screenTime)
                .screen(screen)
                .movie(movie)
                .build();
        em.flush();
        em.clear();

        // when
        long count = scheduleRepository.count();
        scheduleService.removeSchedule(savedSchedule.getId());

        // then
        assertEquals(count - 1, scheduleRepository.count());
        assertFalse(em.find(Screen.class, screen.getId()).getSchedules().contains(savedSchedule));
        assertFalse(em.find(Movie.class, movie.getId()).getSchedules().contains(savedSchedule));
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