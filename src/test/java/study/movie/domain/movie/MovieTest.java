package study.movie.domain.movie;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.global.constants.EntityAttrConst;
import study.movie.repository.movie.MovieRepository;
import study.movie.repository.movie.ReviewRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static study.movie.domain.movie.FilmFormat.*;
import static study.movie.domain.movie.MovieGenre.*;
import static study.movie.domain.movie.FilmRating.*;

@SpringBootTest
@Transactional
@Slf4j
public class MovieTest {
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    ReviewRepository reviewRepository;

    Movie movie;

    @BeforeEach
    @DisplayName("movie 저장 및 review 작성")
    public void setUp() {
        List<String> actors = Arrays.asList("actor1", "actor2", "actor3", "actor4", "actor5");
        List<FilmFormat> formats = Arrays.asList(FOUR_D_FLEX, FOUR_D_FLEX_SCREEN);
        List<MovieGenre> genres = Arrays.asList(DRAMA, ACTION, COMEDY);
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
        Review.createReview(movie, "writer1", 6.56f, "movie comment1");
        Review.createReview(movie, "writer2", 7.45f, "movie comment2");
        Review.createReview(movie, "writer3", 9.55f, "movie comment3");

        movieRepository.save(movie);
    }

    @Test
    public void 영화_엔티티_리뷰_개수_조회() throws Exception {
        // given
        long beforeDBReviewCount = reviewRepository.count();
        long beforeReviewCount = movie.getReviewCount();

        // when
        Review.createReview(movie, "writer4", 9.55f, "movie comment4");
        long afterDBReviewCount = reviewRepository.count();
        long afterReviewCount = movie.getReviewCount();

        // then
        assertThat(afterDBReviewCount).isEqualTo(beforeDBReviewCount + 1);
        assertThat(afterReviewCount).isEqualTo(beforeReviewCount + 1);
    }

    @Test
    public void 영화_엔티티_리뷰_삭제() throws Exception {
        // given
        Review review = Review.createReview(movie, "writer5", 9.55f, "movie comment5");
        long beforeDBReviewCount = reviewRepository.count();
        long beforeReviewCount = movie.getReviewCount();

        // when
        movie.deleteReview(review);
        long afterDBReviewCount = reviewRepository.count();
        long afterReviewCount = movie.getReviewCount();
        // then
        assertThat(afterDBReviewCount).isEqualTo(beforeDBReviewCount - 1);
        assertThat(afterReviewCount).isEqualTo(beforeReviewCount - 1);
    }

    @Test
    public void 영화_엔티티_평점_조회() throws Exception {
        // given
        double asDouble = reviewRepository.findAll().stream()
                .mapToDouble(Review::getScore)
                .average()
                .getAsDouble();
        String savedReviewScoreInDB = String.format("%.2f", asDouble);

        // when
        String savedReviewScoreInMovie = movie.getAverageScore();

        // then
        assertThat(savedReviewScoreInDB).isEqualTo(savedReviewScoreInMovie);
    }

}
