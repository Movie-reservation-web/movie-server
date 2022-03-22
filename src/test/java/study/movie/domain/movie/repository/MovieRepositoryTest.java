package study.movie.domain.movie.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.Movie;
import study.movie.global.constants.EntityAttrConst.FilmFormat;
import study.movie.global.constants.EntityAttrConst.MovieGenre;
import study.movie.repository.movie.MovieRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static study.movie.global.constants.EntityAttrConst.FilmFormat.FOUR_D_FLEX;
import static study.movie.global.constants.EntityAttrConst.FilmFormat.FOUR_D_FLEX_SCREEN;
import static study.movie.global.constants.EntityAttrConst.FilmRating.G_RATED;
import static study.movie.global.constants.EntityAttrConst.MovieGenre.*;

@SpringBootTest
@Transactional
class MovieRepositoryTest {
    @Autowired
    MovieRepository movieRepository;

    @Test
    public void 영화_저장_조회() throws Exception {
        // given
        List<String> actors = Arrays.asList("actor1", "actor2", "actor3", "actor4", "actor5");
        List<FilmFormat> formats = Arrays.asList(FOUR_D_FLEX, FOUR_D_FLEX_SCREEN);
        List<MovieGenre> genres = Arrays.asList(DRAMA, ACTION, COMEDY);
        Movie movie = Movie.builder()
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

        Movie savedMovie = movieRepository.save(movie);

        // when
        Movie findMovie = movieRepository.findById(savedMovie.getId()).orElse(null);

        // then
        Assertions.assertEquals(savedMovie, findMovie);
    }

}