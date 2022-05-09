package study.movie.domain.movie;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.repository.movie.MovieRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
@Slf4j
class MovieRepositoryTest {

    @Autowired
    MovieRepository movieRepository;

    @Test
    public void 영화_생성() {
        // given
        List<String> actors = Arrays.asList("로버트 패틴슨", "폴 다노");
//        ListToCommaSeparatedStringConverter converter = new ListToCommaSeparatedStringConverter();
//        String actorsString = converter.convert(actors);

        List<MovieGenre> genres = Arrays.asList(MovieGenre.ACTION, MovieGenre.ADVENTURE);
        List<FilmFormat> types = Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX);

        Movie movie = Movie.builder()
                .title("더 배트맨")
                .runningTime(176)
                .director("맷 리브스")
                .actors(actors)
                .genres(genres)
                .formats(types)
                .filmRating(FilmRating.UNDETERMINED)
                .nation("한국")
                .releaseDate(LocalDate.of(2022, 3, 1))
                .info("영웅이 될 것인가 악당이 될 것인가")
                .image("이미지")
                .build();

        Movie savedMovie = movieRepository.save(movie);

        // when
        Movie findMovie = movieRepository.findById(savedMovie.getId()).get();

        // then
        Assertions.assertThat(findMovie).isEqualTo(savedMovie);
    }

    @Test
    public void 평점_계산() {
        // given
        List<String> actors = Arrays.asList("로버트 패틴슨", "폴 다노");
        List<MovieGenre> genres = Arrays.asList(MovieGenre.ACTION, MovieGenre.ADVENTURE);
        List<FilmFormat> types = Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX);

        Movie movie = Movie.builder()
                .title("더 배트맨")
                .runningTime(176)
                .director("맷 리브스")
                .actors(actors)
                .genres(genres)
                .formats(types)
                .filmRating(FilmRating.UNDETERMINED)
                .nation("한국")
                .releaseDate(LocalDate.of(2022, 3, 1))
                .info("영웅이 될 것인가 악당이 될 것인가")
                .image("이미지")
                .build();

        Movie savedMovie = movieRepository.save(movie);

        Review review1 = Review.builder()
                .movie(movie)
                .writer("홍길동")
                .score((float) 3.2)
                .comment("평가나쁨")
                .build();

        Review review2 = Review.builder()
                .movie(movie)
                .writer("고길동")
                .score((float) 2.8)
                .comment("평가좋음")
                .build();

        //when
        Movie findMovie = movieRepository.findById(savedMovie.getId()).get();

        //then
        //Assertions.assertThat(findMovie.getAverageScore()).isEqualTo((float)3);
        System.out.print("평점 평균     " + findMovie.getAverageScore());
        //assertEquals(findMovie.getAverageScore(), "3");
    }
}