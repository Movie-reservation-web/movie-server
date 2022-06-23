package study.movie.domain.schedule.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.movie.repository.MovieRepository;
import study.movie.domain.schedule.dto.condition.ScheduleBasicSearchCond;
import study.movie.domain.schedule.entity.Schedule;
import study.movie.domain.schedule.entity.ScreenTime;
import study.movie.domain.theater.entity.Screen;
import study.movie.domain.theater.entity.Theater;
import study.movie.domain.theater.repository.TheaterRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static study.movie.global.utils.NumberUtil.getRandomIndex;

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
    TheaterRepository theaterRepository;
    @Autowired
    MovieRepository movieRepository;

    private Schedule initialRandomSchedule;
    @BeforeEach
    void setUp() {
        initialRandomSchedule = scheduleRepository.findAll()
                .get((int) getRandomIndex(scheduleRepository.count()));
    }

    @Test
    @DisplayName("조건(극장,영화, 상영날짜)에 맞는 상영일정을 검색한다.")
    void 상영일정_조건_검색() {
        // given
        ScreenTime screenTimeForSearch = initialRandomSchedule.getScreenTime();
        Movie movieForSearch = initialRandomSchedule.getMovie();
        Screen screenForSearch = initialRandomSchedule.getScreen();
        Theater theaterForSearch = screenForSearch.getTheater();

        // when
        ScheduleBasicSearchCond cond1 = new ScheduleBasicSearchCond();
        cond1.setTheaterId(theaterForSearch.getId());

        ScheduleBasicSearchCond cond2 = new ScheduleBasicSearchCond();
        cond2.setScreenDate(screenTimeForSearch.getStartDateTime().toLocalDate());
        cond2.setMovieId(movieForSearch.getId());

        ScheduleBasicSearchCond cond3 = new ScheduleBasicSearchCond();
        cond3.setTheaterId(theaterForSearch.getId());
        cond3.setScreenDate(screenTimeForSearch.getStartDateTime().toLocalDate());
        cond3.setMovieId(movieForSearch.getId());

        List<Schedule> findSchedules1 = scheduleRepository.searchBasicSchedules(cond1);
        List<Schedule> findSchedules2 = scheduleRepository.searchBasicSchedules(cond2);
        List<Schedule> findSchedules3 = scheduleRepository.searchBasicSchedules(cond3);
        // then
        for (Schedule schedule : findSchedules1) {
            assertThat(schedule.getScreen().getTheater()).isEqualTo(theaterForSearch);
        }
        for (Schedule schedule : findSchedules2) {
            assertThat(schedule.getScreenTime().getStartDateTime().toLocalDate()).isEqualTo(screenTimeForSearch.getStartDateTime().toLocalDate());
            assertThat(schedule.getMovie()).isEqualTo(movieForSearch);
        }
        for (Schedule schedule : findSchedules3) {
            assertThat(schedule.getScreenTime().getStartDateTime().toLocalDate())
                    .isEqualTo(screenTimeForSearch.getStartDateTime().toLocalDate());
            assertThat(schedule.getMovie()).isEqualTo(movieForSearch);
            assertThat(schedule.getScreen().getTheater()).isEqualTo(theaterForSearch);
        }
    }
}
