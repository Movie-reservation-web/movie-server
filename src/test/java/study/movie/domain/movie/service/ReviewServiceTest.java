package study.movie.domain.movie.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.domain.movie.dto.request.CreateReviewRequest;
import study.movie.domain.movie.dto.request.UpdateReviewRequest;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.movie.entity.Review;
import study.movie.domain.movie.repository.MovieRepository;
import study.movie.domain.movie.repository.ReviewRepository;
import study.movie.global.dto.PostIdResponse;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
public class ReviewServiceTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ReviewService reviewService;

    @Autowired
    EntityManager em;

    @Autowired
    InitService init;

    private List<Review> initialReviewList;
    private List<Movie> initialMovieList;

    @BeforeEach
    void setUp() {
        initialReviewList = reviewRepository.findAll();
        initialMovieList = movieRepository.findAll();
    }

    @Test
    void 리뷰_작성() {
        //given
        Movie movieForWriteReview = initialMovieList.get(0);

        CreateReviewRequest request = new CreateReviewRequest();
        request.setMovieId(movieForWriteReview.getId());
        request.setWriter("작성자");
        request.setComment("내용");
        request.setScore(10f);

        // when
        PostIdResponse response = reviewService.write(request);

        Review findReview = reviewRepository.findById(response.getId()).get();

        //when
        assertThat(findReview.getScore()).isEqualTo(request.getScore());
        assertThat(findReview.getWriter()).isEqualTo(request.getWriter());
        assertThat(findReview.getComment()).isEqualTo(request.getComment());
        assertThat(findReview.getMovie()).isEqualTo(movieForWriteReview);
    }

    @Test
    void 리뷰_수정() {
        //given
        Movie movieForEditReview = initialMovieList.get(0);
        Review reviewForEdit = movieForEditReview.getReviews().get(0);

        // when
        UpdateReviewRequest request = new UpdateReviewRequest();
        request.setComment("내용1");
        request.setScore(10f);

        reviewService.edit(reviewForEdit.getId(), request);

        //when
        assertThat(reviewForEdit.getScore()).isEqualTo(request.getScore());
        assertThat(reviewForEdit.getComment()).isEqualTo(request.getComment());
    }

    @Test
    void 리뷰_삭제() {
        // given
        Movie movieForDeleteReview = initialMovieList.get(0);

        // when
        Review reviewForDelete = movieForDeleteReview.getReviews().get(0);
        reviewService.delete(reviewForDelete.getId());

        // then
        assertThat(reviewRepository.existsById(reviewForDelete.getId())).isFalse();
    }
}
