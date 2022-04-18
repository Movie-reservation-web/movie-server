package study.movie.repository.schedule;

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
import study.movie.domain.schedule.SeatStatus;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.Screen;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.theater.Theater;
import study.movie.dto.schedule.condition.ScheduleSearchCond;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Slf4j
@Rollback
class ScheduleRepositoryTest {
    @Autowired
    EntityManager em;

    @Autowired
    ScheduleRepository scheduleRepository;

    private Theater createTheater(String theaterName, CityCode city, String phone) {
        Theater theater = Theater.builder()
                .name(theaterName)
                .city(city)
                .phone(phone)
                .build();
        em.persist(theater);
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
                .formats(Arrays.asList(FilmFormat.TWO_D,FilmFormat.IMAX))
                .filmRating(FilmRating.G_RATED)
                .genres(Arrays.asList(MovieGenre.values()[0], MovieGenre.values()[1]))
                .image("abc.jpg")
                .info("information")
                .nation("korea")
                .runningTime(160)
                .releaseDate(LocalDate.now())
                .build();
        em.persist(movie);
        return movie;
    }

    @Test
    public void 상영일정_저장_조회() throws Exception {
        // given
        Theater theater = createTheater("용산 CGV", CityCode.SEL, "000-000");
        Screen screen = registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = createMovie("영화1", "홍길동");

        // when
        ScreenTime screenTime = new ScreenTime(LocalDateTime.of(2022, 3, 10, 3, 2, 21), movie.getRunningTime());
        Schedule savedSchedule = Schedule.builder()
                .screenTime(screenTime)
                .screen(screen)
                .movie(movie)
                .build();

        List<Schedule> schedules = scheduleRepository.findAll();

        // then
        assertThat(schedules).containsExactly(savedSchedule);
        assertEquals(screen, schedules.get(0).getScreen());
        assertEquals(movie, schedules.get(0).getMovie());
    }

    @Test
    public void 상영일정_조건_검색() throws Exception {
        // given
        String theaterName = "CGV 용산";
        Theater theater = createTheater(theaterName, CityCode.SEL, "000-000");
        Screen screen = registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        String title = "영화1";
        Movie movie = createMovie(title, "홍길동");
        ScreenTime screenTime = new ScreenTime(LocalDateTime.of(2022, 3, 10, 3, 2, 21), movie.getRunningTime());
        Schedule savedSchedule = Schedule.builder()
                .screenTime(screenTime)
                .screen(screen)
                .movie(movie)
                .build();

        // when
        ScheduleSearchCond cond1 = new ScheduleSearchCond();
        cond1.setTheaterName(theaterName);

        ScheduleSearchCond cond2 = new ScheduleSearchCond();
        cond2.setScreenDate(screenTime.getStartDateTime().toLocalDate());
        cond2.setMovieTitle(title);

        ScheduleSearchCond cond3 = new ScheduleSearchCond();
        cond3.setTheaterName(theaterName);
        cond3.setScreenDate(screenTime.getStartDateTime().toLocalDate());
        cond3.setMovieTitle(title);


        List<Schedule> schedules1 = scheduleRepository.searchSchedules(cond1);
        List<Schedule> schedules2 = scheduleRepository.searchSchedules(cond2);
        List<Schedule> schedules3 = scheduleRepository.searchSchedules(cond3);

        // then
        assertThat(schedules1).containsExactly(savedSchedule);
        assertThat(schedules2).containsExactly(savedSchedule);
        assertThat(schedules3).containsExactly(savedSchedule);
    }

    @Test
    public void 상영일정_좌석수_조회() throws Exception {
        // given
        Theater theater = createTheater("용산 CGV", CityCode.SEL, "000-000");
        Screen screen = registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = createMovie("영화1", "홍길동");
        ScreenTime screenTime = new ScreenTime(LocalDateTime.of(2022, 3, 10, 3, 2, 21), movie.getRunningTime());
        Schedule savedSchedule = Schedule.builder()
                .screenTime(screenTime)
                .screen(screen)
                .movie(movie)
                .build();

        em.flush();

        // when
        int totalSeatCount = screen.getMaxRows() * screen.getMaxCols();
        Schedule findSchedule = scheduleRepository.findById(savedSchedule.getId()).get();

        // then
        assertEquals(totalSeatCount,findSchedule.getReservedSeatCount(SeatStatus.EMPTY));
        assertEquals(totalSeatCount,findSchedule.getTotalSeatCount());
    }



}