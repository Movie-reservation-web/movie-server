package study.movie.service.schedule;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
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
import study.movie.dto.schedule.*;
import study.movie.repository.schedule.ScheduleRepository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

    private Movie createMovie(String title, String director, List<FilmFormat> formats) {
        Movie movie = Movie.builder()
                .title(title)
                .director(director)
                .actors(Arrays.asList("aa", "bb"))
                .formats(formats)
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
    public void 상영일정_저장() throws Exception {
        // given
        Theater theater = createTheater("용산 CGV", CityCode.SEL, "000-000");
        Screen screen = registerScreen("1관", ScreenFormat.NORMAL, theater, 3, 3);
        Movie movie = createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
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
        Theater theater = createTheater(theaterName, CityCode.SEL, "000-000");
        Screen screen = registerScreen("1관", ScreenFormat.NORMAL, theater, 3, 3);
        Movie movie = createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        LocalDate screenDate = LocalDate.of(2022, 3, 10);
        ScreenTime screenTime1 = new ScreenTime(screenDate.atTime(3, 2, 21), movie);
        ScreenTime screenTime2 = new ScreenTime(screenDate.atTime(6, 2, 21), movie);
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
    public void 상영일정_조건_검색_상영관_포멧() throws Exception {
        // given
        String theaterName = "CGV 용산";
        Theater theater = createTheater(theaterName, CityCode.SEL, "000-000");
        Screen screen1 = registerScreen("1관", ScreenFormat.NORMAL, theater, 3, 3);
        Screen screen2 = registerScreen("2관", ScreenFormat.FOUR_D_FLEX, theater, 3, 3);
        Movie movie = createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        LocalDate screenDate = LocalDate.of(2022, 3, 10);
        ScreenTime screenTime = new ScreenTime(screenDate.atTime(3, 2, 21), movie);
        Schedule.builder()
                .screenTime(screenTime)
                .screen(screen1)
                .movie(movie)
                .build();

        Schedule.builder()
                .screenTime(screenTime)
                .screen(screen2)
                .movie(movie)
                .build();

        FilmFormat filmFormat = FilmFormat.TWO_D;
        ScheduleSearchCond cond1 = new ScheduleSearchCond();
        cond1.setFormat(filmFormat);
        cond1.setTheaterName(theaterName);
        cond1.setMovieTitle(movie.getTitle());
        cond1.setScreenDate(screenDate);
        cond1.setFinalSearch(true);

        ScheduleSearchCond cond2 = new ScheduleSearchCond();
        cond2.setFormat(filmFormat);
        cond2.setMovieTitle(movie.getTitle());
        cond2.setFinalSearch(false);

        // 상영관1 -> 일반(2D) , 상영관2 -> 4DX(2D)
        // 조건 -> 영화 포맷(2D)
        // when
        List<ScheduleScreenResponse> screenResponses = (List<ScheduleScreenResponse>) scheduleService.searchSchedules(cond1);
        List<ScheduleSearchResponse> searchResponses = (List<ScheduleSearchResponse>) scheduleService.searchSchedules(cond2);

        // then
        assertEquals(1, screenResponses.size());
        assertTrue(ScreenFormat.NORMAL.getValue().equals(screenResponses.get(0).getScreenFormat()));
        assertEquals(cond1.getScreenDate(), screenResponses.get(0).getScreenTime().getStartDateTime().toLocalDate());

        assertEquals(1, searchResponses.size());
        assertEquals(searchResponses.get(0).getMovie().getMovieTitle(), cond2.getMovieTitle());
    }

    @Test
    public void 모든_상영일정_조회() throws Exception {
        // given
        String theaterName = "CGV 용산";
        Theater theater = createTheater(theaterName, CityCode.SEL, "000-000");
        Screen screen = registerScreen("1관", ScreenFormat.NORMAL, theater, 3, 3);
        Movie movie = createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        LocalDate screenDate = LocalDate.of(2022, 3, 10);
        ScreenTime screenTime1 = new ScreenTime(screenDate.atTime(3, 2, 21), movie);
        ScreenTime screenTime2 = new ScreenTime(screenDate.atTime(6, 2, 21), movie);

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
        Theater theater = createTheater("용산 CGV", CityCode.SEL, "000-000");
        Screen screen = registerScreen("1관", ScreenFormat.NORMAL, theater, 3, 3);
        Movie movie = createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        ScreenTime screenTime = new ScreenTime(LocalDateTime.of(2022, 5, 10, 3, 2, 21), movie);

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
}