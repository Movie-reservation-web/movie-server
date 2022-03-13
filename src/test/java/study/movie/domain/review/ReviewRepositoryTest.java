package study.movie.domain.review;

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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
class ReviewRepositoryTest {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Test
    void 리뷰를_조회한다() {
        // given
        List<String> actors = Arrays.asList("로버트 패틴슨", "폴 다노");
        ListToCommaSeparatedStringConverter converter = new ListToCommaSeparatedStringConverter();
        String actorsString = converter.convert(actors);

        List<GenreType> genres = Arrays.asList(GenreType.ACTION);
        List<MovieType> types = Arrays.asList(MovieType.IMAX, MovieType.THREE_DIMENSIONS);

        Movie movie = Movie.builder()
                .name("더 배트맨")
                .time(176L)
                .info("영웅이 될 것인가 악당이 될 것인가")
                .release(LocalDate.of(2022, 3, 1))
                .director("맷 리브스")
                .actors(actorsString)
                .genres(genres)
                .types(types)
                .build();

        Movie savedMovie = movieRepository.save(movie);

        Review review = Review.builder()
                .movie(savedMovie)
                .score(5L)
                .review("괜찮은 영화네요")
                .reservedId(UUID.randomUUID().toString())
                .build();

        Review savedReview = reviewRepository.save(review);

        //when
        Review findReview = reviewRepository.findById(savedReview.getId()).get();

        //then
        Assertions.assertThat(findReview).isEqualTo(savedReview);
    }


}