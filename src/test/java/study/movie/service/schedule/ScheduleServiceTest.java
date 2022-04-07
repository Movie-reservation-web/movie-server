package study.movie.service.schedule;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.FilmRating;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.MovieGenre;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.Screen;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.theater.Theater;
import study.movie.dto.schedule.CreateScheduleRequest;
import study.movie.dto.schedule.CreateScheduleResponse;
import study.movie.dto.schedule.ScheduleSearchCond;
import study.movie.dto.schedule.ScheduleSearchResponse;
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
@Commit
@Slf4j
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

    private Movie createMovie(String title, String director) {
        Movie movie = Movie.builder()
                .title(title)
                .director(director)
                .actors(Arrays.asList("aa", "bb"))
                .formats(Arrays.asList(FilmFormat.values()[0], FilmFormat.values()[1]))
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
        Screen screen = registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = createMovie("영화1", "홍길동");
        LocalDateTime startTime = LocalDateTime.of(2022, 5, 10, 3, 2, 21);

        // when
        CreateScheduleRequest request = new CreateScheduleRequest(startTime, movie.getId(), screen.getId());
        CreateScheduleResponse response = scheduleService.save(request);

        // then
        assertEquals(theater.getName(), response.getTheaterName());
        assertEquals(movie.getTitle(), response.getMovieTitle());
        assertEquals(startTime, response.getStartTime());
    }

    @Test
    public void 상영일정_조건_검색_중복된_값_제거() throws Exception {
        // given
        String theaterName = "CGV 용산";
        Theater theater = createTheater(theaterName, CityCode.SEL, "000-000");
        Screen screen = registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = createMovie("영화1", "홍길동");
        LocalDate screenDate = LocalDate.of(2022, 3, 10);
        LocalDateTime startTime1 = screenDate.atTime(3, 2, 21);
        LocalDateTime startTime2 = screenDate.atTime(6, 2, 21);
        Schedule savedSchedule1 = Schedule.builder()
                .startTime(startTime1)
                .screen(screen)
                .movie(movie)
                .build();

        Schedule savedSchedule2 = Schedule.builder()
                .startTime(startTime2)
                .screen(screen)
                .movie(movie)
                .build();

        ScheduleSearchCond cond = new ScheduleSearchCond();
        cond.setTheaterName(theaterName);
        cond.setFinalSearch(false);

        // when
        List<ScheduleSearchResponse> scheduleSearchRespons = (List<ScheduleSearchResponse>) scheduleService.searchSchedules(cond);
        List<Schedule> schedules = scheduleRepository.searchSchedules(cond);
        long dbCount = schedules.size();

        // then
        assertThat(schedules).containsExactly(savedSchedule1,savedSchedule2);
        assertEquals(dbCount-1, scheduleSearchRespons.size(),"시간은 다르지만 날짜가 같은 객체는 중복제거되어야 한다.");
        assertEquals(screenDate, scheduleSearchRespons.get(0).getScreenDate());
    }

    @Test
    public void 모든_상영일정_조회() throws Exception {
        // given
        String theaterName = "CGV 용산";
        Theater theater = createTheater(theaterName, CityCode.SEL, "000-000");
        Screen screen = registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = createMovie("영화1", "홍길동");
        LocalDate screenDate = LocalDate.of(2022, 3, 10);
        LocalDateTime startTime1 = screenDate.atTime(3, 2, 21);
        LocalDateTime startTime2 = screenDate.atTime(6, 2, 21);

        // when
        Schedule savedSchedule1 = Schedule.builder()
                .startTime(startTime1)
                .screen(screen)
                .movie(movie)
                .build();
        Schedule savedSchedule2 = Schedule.builder()
                .startTime(startTime2)
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
        Screen screen = registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = createMovie("영화1", "홍길동");
        LocalDateTime startTime = LocalDateTime.of(2022, 5, 10, 3, 2, 21);

        Schedule savedSchedule = Schedule.builder()
                .startTime(startTime)
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