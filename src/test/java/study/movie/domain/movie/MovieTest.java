package study.movie.domain.movie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.repository.MovieRepository;
import study.movie.domain.movie.repository.ReviewRepository;
import study.movie.global.constants.EntityAttrConst;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.movie.global.constants.EntityAttrConst.FilmFormat.FOUR_D_FLEX;
import static study.movie.global.constants.EntityAttrConst.FilmFormat.FOUR_D_FLEX_SCREEN;
import static study.movie.global.constants.EntityAttrConst.FilmRating.G_RATED;
import static study.movie.global.constants.EntityAttrConst.MovieGenre.*;

@SpringBootTest
@Transactional
@Commit
public class MovieTest {
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    ReviewRepository reviewRepository;

    Movie findMovie;

    @BeforeEach
    @DisplayName("movie 저장 및 review 작성")
    public void setUp() {
        List<String> actors = Arrays.asList("actor1", "actor2", "actor3", "actor4", "actor5");
        List<EntityAttrConst.FilmFormat> formats = Arrays.asList(FOUR_D_FLEX, FOUR_D_FLEX_SCREEN);
        List<EntityAttrConst.MovieGenre> genres = Arrays.asList(DRAMA, ACTION, COMEDY);
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
        findMovie = movieRepository.findById(savedMovie.getId()).get();


    }

    @Test
    public void 영화_엔티티_리뷰_개수_조회() throws Exception {
        // given
        Review review1 = Review.createReview(findMovie, "writer1", 6.56f, "movie comment1");
        Review review2 = Review.createReview(findMovie, "writer2", 7.45f, "movie comment2");
        Review review3 = Review.createReview(findMovie, "writer3", 9.55f, "movie comment3");

        // when
        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);

        // then
        assertThat(findMovie.getReviewCount()).isEqualTo(3);
    }

    @Test()
    public void 영화_엔티티_리뷰_삭제() throws Exception {
        // given
        Review review1 = Review.createReview(findMovie, "writer1", 6.56f, "movie comment1");
        Review review2 = Review.createReview(findMovie, "writer2", 7.45f, "movie comment2");
        Review review3 = Review.createReview(findMovie, "writer3", 9.55f, "movie comment3");

        Review savedReview1 = reviewRepository.save(review1);
        Review savedReview2 = reviewRepository.save(review2);
        Review savedReview3 = reviewRepository.save(review3);

        // when
        reviewRepository.delete(savedReview1);
//        Review findReview1 = reviewRepository.findById(savedReview1.getId()).get();
//        findMovie.deleteReview(findReview1);

        // then
        assertThat(findMovie.getReviewCount()).isEqualTo(2);
//        assertThatThrownBy(() -> reviewRepository.findById(savedReview1.getId()))
//                .isInstanceOf(Exception.class);

    }

    @Test
    public void 영화_엔티티_평점_조회() throws Exception {
        // given

        // when

        // then
    }

}
