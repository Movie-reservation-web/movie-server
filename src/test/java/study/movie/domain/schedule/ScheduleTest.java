package study.movie.domain.schedule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.Movie;
import study.movie.repository.movie.MovieRepository;
import study.movie.repository.schedule.ScheduleRepository;
import study.movie.domain.theater.Screen;
import study.movie.domain.theater.Seat;
import study.movie.repository.theater.ScreenRepository;
import study.movie.global.constants.EntityAttrConst;
import study.movie.global.constants.EntityAttrConst.FilmFormat;
import study.movie.global.constants.EntityAttrConst.ScreenFormat;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static study.movie.global.constants.EntityAttrConst.FilmFormat.FOUR_D_FLEX;
import static study.movie.global.constants.EntityAttrConst.FilmRating.G_RATED;
import static study.movie.global.constants.EntityAttrConst.MovieGenre.*;
import static study.movie.global.constants.EntityAttrConst.ScreenFormat.GOLD_CLASS;
import static study.movie.global.constants.EntityAttrConst.ScreenFormat.SCREEN_X;

@SpringBootTest
@Transactional
public class ScheduleTest {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ScreenRepository screenRepository;

    Movie movie;
    Screen screen;

    @BeforeEach
    public void setUp() {
        List<String> actors = Arrays.asList("actor1", "actor2", "actor3", "actor4", "actor5");
        List<FilmFormat> formats = Arrays.asList(FOUR_D_FLEX, FilmFormat.FOUR_D_FLEX_SCREEN);
        List<EntityAttrConst.MovieGenre> genres = Arrays.asList(DRAMA, ACTION, COMEDY);
        movie = Movie.builder()
                .title("title")
                .director("director")
                .actors(actors)
                .genres(genres)
                .filmRating(G_RATED)
                .runningTime(160)
                .nation("KOREA")
                .releaseDate(LocalDate.of(2022, 3, 1))
                .formats(formats)
                .info("movie information")
                .image("image1.jpg")
                .build();
        movieRepository.save(movie);
        List<ScreenFormat> screenFormats = Arrays.asList(ScreenFormat.FOUR_D_FLEX_SCREEN, SCREEN_X, GOLD_CLASS);
        List<Seat> seats = new ArrayList<>();
        IntStream.range(0,10).forEach(i -> seats.add(Seat.createSeat('A', i)));
        screen = Screen.builder()
                .name("1관")
                .formats(screenFormats)
                .seats(seats)
                .build();
        screenRepository.save(screen);
    }

    @Test
    public void 상영_일정_추가() throws Exception {
        // given
        Schedule schedule = Schedule.createSchedule(movie, screen, LocalDateTime.now());

        // when
        Movie findMovie = movieRepository.findById(schedule.getMovie().getId()).get();
        Screen findScreen = screenRepository.findById(schedule.getScreen().getId()).get();

        // then
        Assertions.assertEquals(findMovie, movie);
        Assertions.assertEquals(findScreen, screen);
    }
    
    @Test
    public void 상영_종료_시간_계산() throws Exception {
        // given
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
        LocalDateTime comparisonEndTime = startTime.plus(Duration.ofMinutes(movie.getRunningTime()));

        Schedule schedule = Schedule.createSchedule(movie, screen, startTime);

        // when
        LocalDateTime endTime = schedule.getEndTime();

        // then
        Assertions.assertEquals(comparisonEndTime, endTime);
    }
        

}
