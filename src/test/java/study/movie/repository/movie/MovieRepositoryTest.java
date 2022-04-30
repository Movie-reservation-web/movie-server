package study.movie.repository.movie;

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
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.Screen;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.theater.Theater;
import study.movie.repository.schedule.ScheduleRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
@Rollback
class MovieRepositoryTest {
    @Autowired
    EntityManager em;

    @Autowired
    MovieRepository movieRepository;
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    InitService init;

    @Test
    void 상영중인_영화_차트_중복_제거() {
        // given
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        movie.addAudience(10);

        ScreenTime screenTime = new ScreenTime(LocalDateTime.now().plusDays(5), movie.getRunningTime());
        ScreenTime screenTime1 = new ScreenTime(LocalDateTime.now().plusDays(4), movie.getRunningTime());
        Schedule savedSchedule = Schedule.builder()
                .screenTime(screenTime)
                .screen(screen)
                .movie(movie)
                .build();
        Schedule savedSchedule2 = Schedule.builder()
                .screenTime(screenTime1)
                .screen(screen)
                .movie(movie)
                .build();
        scheduleRepository.save(savedSchedule);
        scheduleRepository.save(savedSchedule2);

        // when
        List<Movie> movieCharts = movieRepository.findMovieByOpenStatus();

        // then
        assertThat(movieCharts.size()).isEqualTo(1);
        assertThat(movieCharts.get(0)).isEqualTo(movie);
    }

    @Test
    void 상영중인_영화_차트_내림차순_정렬() {
        // given
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        movie.addAudience(10);
        Movie movie1 = init.createMovie("영화2", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        movie1.addAudience(20);

        ScreenTime screenTime = new ScreenTime(LocalDateTime.now().plusDays(5), movie.getRunningTime());
        ScreenTime screenTime1 = new ScreenTime(LocalDateTime.now().plusDays(4), movie.getRunningTime());
        Schedule savedSchedule = Schedule.builder()
                .screenTime(screenTime)
                .screen(screen)
                .movie(movie)
                .build();
        Schedule savedSchedule2 = Schedule.builder()
                .screenTime(screenTime1)
                .screen(screen)
                .movie(movie1)
                .build();

        scheduleRepository.save(savedSchedule);
        scheduleRepository.save(savedSchedule2);


        // when
        List<Movie> movieCharts = movieRepository.findMovieByOpenStatus();

        // then
        assertThat(movieCharts.size()).isEqualTo(2);
        assertThat(movieCharts.get(0)).isEqualTo(movie1);
        assertThat(movieCharts.get(1)).isEqualTo(movie);
    }

}