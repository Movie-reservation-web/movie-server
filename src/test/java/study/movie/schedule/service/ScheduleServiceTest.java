package study.movie.schedule.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.domain.movie.entity.FilmFormat;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.schedule.entity.Schedule;
import study.movie.domain.schedule.entity.ScreenTime;
import study.movie.domain.schedule.service.ScheduleServiceImpl;
import study.movie.domain.theater.entity.CityCode;
import study.movie.domain.theater.entity.Screen;
import study.movie.domain.theater.entity.ScreenFormat;
import study.movie.domain.theater.entity.Theater;
import study.movie.domain.schedule.dto.condition.ScheduleBasicSearchCond;
import study.movie.domain.schedule.dto.request.CreateScheduleRequest;
import study.movie.domain.schedule.dto.request.ScheduleScreenRequest;
import study.movie.domain.schedule.dto.response.MovieFormatResponse;
import study.movie.domain.schedule.dto.response.ScheduleScreenResponse;
import study.movie.domain.schedule.dto.response.ScheduleSearchResponse;
import study.movie.global.dto.IdListRequest;
import study.movie.global.dto.PostIdResponse;
import study.movie.domain.movie.repository.ReviewRepository;
import study.movie.domain.schedule.repository.ScheduleRepository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static study.movie.domain.theater.entity.ScreenFormat.*;

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
    public void ????????????_??????() throws Exception {
        // given
        String theaterName = "?????? CGV";
        Theater theater = init.createTheater(theaterName, CityCode.SEL);
        Screen screen = init.registerScreen("1???", TWO_D, theater, 3, 3);
        Movie movie = init.createBasicMovie();
        LocalDateTime startTime = LocalDateTime.now().plusDays(10);
        em.flush();
        Long movieId = movie.getId();
        Long screenId = screen.getId();
        em.clear();

        // when
        CreateScheduleRequest request = new CreateScheduleRequest();
        request.setMovieId(movieId);
        request.setScreenId(screenId);
        request.setStartTime(startTime);

        PostIdResponse response = scheduleService.save(request);

        // then
        Schedule schedule = scheduleRepository.findById(response.getId()).get();
        assertThat(theaterName).isEqualTo(schedule.getScreen().getTheater().getName());
        assertThat(movie.getTitle()).isEqualTo(schedule.getMovie().getTitle());
        assertThat(startTime).isEqualTo(schedule.getScreenTime().getStartDateTime());
    }

    @Test
    public void ????????????_??????_??????_?????????_???_??????() throws Exception {
        // given
        String theaterName = "CGV ??????";
        Theater theater = init.createTheater(theaterName, CityCode.SEL);
        Screen screen = init.registerScreen("1???", TWO_D, theater, 3, 3);
        Movie movie = init.createBasicMovie();
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
        cond.setTheaterId(theater.getId());

        // when
        List<ScheduleSearchResponse> scheduleSearchResponse = scheduleService.searchBasicSchedules(cond);
        List<Schedule> schedules = scheduleRepository.searchBasicSchedules(cond);
        long dbCount = schedules.size();

        // then
        assertThat(schedules).containsExactly(savedSchedule1, savedSchedule2);
        assertEquals(dbCount - 1, scheduleSearchResponse.size(), "????????? ???????????? ????????? ?????? ????????? ????????????????????? ??????.");
        assertEquals(screenDate, scheduleSearchResponse.get(0).getScreenDate());
    }

    @Test
    public void ????????????_??????_??????_??????_??????() throws Exception {
        // given
        String theaterName1 = "CGV ??????";
        String theaterName2 = "CGV ??????";
        List<Screen> screens1 = init.addTheaterScreen(
                theaterName1,
                CityCode.SEL,
                Arrays.asList("1???", "2???", "3???", "4???", "5???"),
                Arrays.asList(TWO_D, IMAX, FOUR_D_FLEX, SCREEN_X, PREMIUM));
        List<Screen> screens2 = init.addTheaterScreen(
                theaterName2,
                CityCode.SEL,
                Arrays.asList("1???", "2???", "3???", "4???"),
                Arrays.asList(TWO_D, TWO_D, FOUR_D_FLEX, PREMIUM));

        List<Movie> movies = init.addMovies(
                Arrays.asList("??????1", "??????2", "??????3", "??????4", "??????5", "??????6"),
                Arrays.asList("??????1", "??????2", "??????3", "??????4", "??????5", "??????6"),
                Arrays.asList(
                        Arrays.asList("??????1", "??????2", "??????3"),
                        Arrays.asList("??????1", "??????2", "??????3"),
                        Arrays.asList("??????1", "??????2", "??????3"),
                        Arrays.asList("??????1", "??????2", "??????3"),
                        Arrays.asList("??????1", "??????2", "??????3"),
                        Arrays.asList("??????1", "??????2", "??????3")
                ),
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
        // CGV ??????, ?????????1, ??????1, 2D
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens1.get(0))
                .movie(savedMovie)
                .build();
        // CGV ??????, ?????????2, ??????1, 2D
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens2.get(1))
                .movie(savedMovie)
                .build();
        // CGV ??????, ?????????2, ??????1, IMAX
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens1.get(1))
                .movie(savedMovie)
                .build();
        // CGV ??????, ?????????3, ??????2, 4DX
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens2.get(2))
                .movie(movies.get(3))
                .build();

        ScreenFormat screenFormat = TWO_D;
        // ?????? ?????? -> ??????1(2D)
        ScheduleBasicSearchCond cond = new ScheduleBasicSearchCond();
        cond.setScreenFormat(screenFormat);
        cond.setMovieId(savedMovie.getId());

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
    public void ????????????_??????_??????_?????????_??????() throws Exception {
        // given
        String theaterName1 = "CGV ??????";
        String theaterName2 = "CGV ??????";
        List<Screen> screens1 = init.addTheaterScreen(
                theaterName1,
                CityCode.SEL,
                Arrays.asList("1???", "2???", "3???", "4???", "5???"),
                Arrays.asList(TWO_D, IMAX, FOUR_D_FLEX, SCREEN_X, PREMIUM));
        Long theaterId1 = screens1.get(0).getTheater().getId();
        List<Screen> screens2 = init.addTheaterScreen(
                theaterName2,
                CityCode.SEL,
                Arrays.asList("1???", "2???", "3???", "4???"),
                Arrays.asList(TWO_D, TWO_D, FOUR_D_FLEX, PREMIUM));
        Long theaterId2 = screens2.get(0).getTheater().getId();

        List<Movie> movies = init.addMovies(
                Arrays.asList("??????1", "??????2", "??????3", "??????4", "??????5", "??????6"),
                Arrays.asList("??????1", "??????2", "??????3", "??????4", "??????5", "??????6"),
                Arrays.asList(
                        Arrays.asList("??????1", "??????2", "??????3"),
                        Arrays.asList("??????1", "??????2", "??????3"),
                        Arrays.asList("??????1", "??????2", "??????3"),
                        Arrays.asList("??????1", "??????2", "??????3"),
                        Arrays.asList("??????1", "??????2", "??????3"),
                        Arrays.asList("??????1", "??????2", "??????3")
                ),
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
        // CGV ??????, ?????????1, ??????1, 2D
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens1.get(0))
                .movie(savedMovie)
                .build();
        // CGV ??????, ?????????2, ??????1, 2D
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens2.get(1))
                .movie(savedMovie)
                .build();
        // CGV ??????, ?????????2, ??????1, IMAX
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens1.get(1))
                .movie(savedMovie)
                .build();
        // CGV ??????, ?????????3, ??????2, 4DX
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens2.get(2))
                .movie(movies.get(3))
                .build();

        ScreenFormat screenFormat = TWO_D;
        // ?????? ?????? -> ??????1(??????), CGV ??????, ???????????? 22/03/10
        ScheduleScreenRequest request = new ScheduleScreenRequest();
        request.setTheaterIds(Arrays.asList(theaterId1));
        request.setMovieId(savedMovie.getId());
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
    public void ????????????_??????_??????_??????() throws Exception {
        String theaterName1 = "CGV ??????";
        String theaterName2 = "CGV ??????";
        List<Screen> screens1 = init.addTheaterScreen(
                theaterName1,
                CityCode.SEL,
                Arrays.asList("1???", "2???", "3???", "4???", "5???"),
                Arrays.asList(TWO_D, IMAX, FOUR_D_FLEX, SCREEN_X, PREMIUM));
        List<Screen> screens2 = init.addTheaterScreen(
                theaterName2,
                CityCode.SEL,
                Arrays.asList("1???", "2???", "3???", "4???"),
                Arrays.asList(TWO_D, TWO_D, FOUR_D_FLEX, PREMIUM));

        List<Movie> movies = init.addMovies(
                Arrays.asList("??????1", "??????2", "??????3", "??????4", "??????5", "??????6"),
                Arrays.asList("??????1", "??????2", "??????3", "??????4", "??????5", "??????6"),
                Arrays.asList(
                        Arrays.asList("??????1", "??????2", "??????3"),
                        Arrays.asList("??????1", "??????2", "??????3"),
                        Arrays.asList("??????1", "??????2", "??????3"),
                        Arrays.asList("??????1", "??????2", "??????3"),
                        Arrays.asList("??????1", "??????2", "??????3"),
                        Arrays.asList("??????1", "??????2", "??????3")
                ),
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
        // CGV ??????, ?????????1, ??????1, 2D
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens1.get(0))
                .movie(savedMovie)
                .build();
        // CGV ??????, ?????????2, ??????1, 2D
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens2.get(1))
                .movie(savedMovie)
                .build();
        // CGV ??????, ?????????2, ??????1, IMAX
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens1.get(1))
                .movie(savedMovie)
                .build();
        // CGV ??????, ?????????3, ??????2, 4DX
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screens2.get(2))
                .movie(movies.get(3))
                .build();

        // when
        // ?????? ?????? -> ?????? 1
        MovieFormatResponse response = scheduleService.searchScheduleByMovie(savedMovie.getTitle());

        // then
        assertEquals(savedMovie.getTitle(), response.getMovieTitle());
        assertThat(response.getFormats()).containsExactly(TWO_D, IMAX);
    }

    @Test
    public void ??????_????????????_??????() throws Exception {
        // given
        String theaterName = "CGV ??????";
        Theater theater = init.createTheater(theaterName, CityCode.SEL);
        Screen screen = init.registerScreen("1???", TWO_D, theater, 3, 3);
        Movie movie = init.createBasicMovie();
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
    public void ????????????_??????() throws Exception {
        // given
        Theater theater = init.createTheater("?????? CGV", CityCode.SEL);
        Screen screen = init.registerScreen("1???", TWO_D, theater, 3, 3);
        Movie movie = init.createBasicMovie();
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
        IdListRequest request = new IdListRequest();
        request.setIds(Collections.singletonList(savedSchedule.getId()));
        scheduleService.removeSchedule(request);
        // then
        assertEquals(count - 1, scheduleRepository.count());
        assertFalse(em.find(Screen.class, screen.getId()).getSchedules().contains(savedSchedule));
        assertFalse(em.find(Movie.class, movie.getId()).getSchedules().contains(savedSchedule));
    }
}
