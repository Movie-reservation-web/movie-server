package study.movie.domain.screening;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.converter.ListToCommaSeparatedStringConverter;
import study.movie.domain.movie.GenreType;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.MovieRepository;
import study.movie.domain.movie.MovieType;
import study.movie.domain.screen.Screen;
import study.movie.domain.screen.ScreenRepository;
import study.movie.domain.screen.ScreenType;
import study.movie.domain.theather.Theater;
import study.movie.domain.theather.TheaterRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
class ScreeningRepositoryTest {

    @Autowired
    ScreeningRepository screeningRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ScreenRepository screenRepository;

    @Autowired
    TheaterRepository theaterRepository;

    @Test
    void 상영일정을_조회한다() {
        // given
        Movie movie = getMovie();
        Movie savedMovie = movieRepository.save(movie);

        Theater theater = getTheater();
        Theater savedTheater = theaterRepository.save(theater);

        Screen screen = getScreen(savedTheater);
        Screen savedScreen = screenRepository.save(screen);

        Screening screening = Screening.builder()
                .startTime(LocalDateTime.now())
                .movie(savedMovie)
                .screen(savedScreen)
                .build();

        Screening savedScreening = screeningRepository.save(screening);

        //when
        Screening findScreening = screeningRepository.findById(savedScreening.getId()).get();

        //then
        Assertions.assertThat(findScreening).isEqualTo(savedScreening);
    }

    private Movie getMovie() {
        List<String> actors = Arrays.asList("로버트 패틴슨", "폴 다노");
        ListToCommaSeparatedStringConverter converter = new ListToCommaSeparatedStringConverter();
        String actorsString = converter.convert(actors);

        List<GenreType> genres = Arrays.asList(GenreType.ACTION);
        List<MovieType> types = Arrays.asList(MovieType.IMAX, MovieType.THREE_DIMENSIONS);

        return Movie.builder()
                .name("더 배트맨")
                .time(176L)
                .info("영웅이 될 것인가 악당이 될 것인가")
                .release(LocalDate.of(2022, 3, 1))
                .director("맷 리브스")
                .actors(actorsString)
                .genres(genres)
                .types(types)
                .build();
    }

    private Theater getTheater() {
        Theater theater = Theater.builder()
                .city("서울")
                .name("영등포 CGV")
                .phone("02-4940-1002")
                .build();

        return theater;
    }

    private Screen getScreen(Theater theater) {
        Screen screen = Screen.builder()
                .name("6관")
                .capacity(320L)
                .screenRow(10L)
                .screenColumn(32L)
                .type(ScreenType.IMAX)
                .theater(theater)
                .build();

        return screen;
    }
}