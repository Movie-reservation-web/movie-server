package study.movie.repository.schedule;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.Movie;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.ScreenTime;
import study.movie.domain.schedule.SeatStatus;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.Screen;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.theater.Theater;
import study.movie.dto.schedule.condition.ScheduleBasicSearchCond;

import javax.persistence.EntityManager;
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

    @Autowired
    InitService init;

    @Test
    void 상영일정_조건_검색() {
        // given
        String theaterName = "CGV 용산";
        Theater theater = init.createTheater(theaterName, CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        String title = "영화1";
        Movie movie = init.createMovie(title, "홍길동", Arrays.asList(FilmFormat.TWO_D));

        ScreenTime screenTime = new ScreenTime(LocalDateTime.of(2022, 3, 10, 3, 2, 21), movie.getRunningTime());
        Schedule savedSchedule = Schedule.builder()
                .screenTime(screenTime)
                .screen(screen)
                .movie(movie)
                .build();

        // when
        ScheduleBasicSearchCond cond1 = new ScheduleBasicSearchCond();
        cond1.setTheaterId(theater.getId());

        ScheduleBasicSearchCond cond2 = new ScheduleBasicSearchCond();
        cond2.setScreenDate(screenTime.getStartDateTime().toLocalDate());
        cond2.setMovieId(movie.getId());

        ScheduleBasicSearchCond cond3 = new ScheduleBasicSearchCond();
        cond3.setTheaterId(theater.getId());
        cond3.setScreenDate(screenTime.getStartDateTime().toLocalDate());
        cond3.setMovieId(movie.getId());


        List<Schedule> schedules1 = scheduleRepository.searchBasicSchedules(cond1);
        List<Schedule> schedules2 = scheduleRepository.searchBasicSchedules(cond2);
        List<Schedule> schedules3 = scheduleRepository.searchBasicSchedules(cond3);

        // then
        assertThat(schedules1).containsExactly(savedSchedule);
        assertThat(schedules2).containsExactly(savedSchedule);
        assertThat(schedules3).containsExactly(savedSchedule);
    }

    @Test
    void 상영일정_좌석수_조회() {
        // given
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));

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
        assertEquals(totalSeatCount, findSchedule.getSeatCount(SeatStatus.EMPTY));
        assertEquals(totalSeatCount, findSchedule.getTotalSeatCount());
    }

}