package study.movie.domain.movie.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.dto.request.CreateReviewRequest;
import study.movie.domain.movie.dto.request.UpdateReviewRequest;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.movie.entity.Review;
import study.movie.domain.movie.repository.MovieRepository;
import study.movie.domain.movie.repository.ReviewRepository;
import study.movie.global.dto.PostIdResponse;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static study.movie.global.utils.NumberUtil.getRandomIndex;

@SpringBootTest
@Transactional
@Slf4j
@Rollback
public class ReviewServiceTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ReviewService reviewService;

    @Autowired
    EntityManager em;

    private Movie initialRandomMovie;

    @BeforeEach
    void setUp() {
        initialRandomMovie = movieRepository.findAll()
                .get((int) getRandomIndex(movieRepository.count()));
    }

    @Test
    void 리뷰_작성() {
        //given
        CreateReviewRequest request = new CreateReviewRequest();
        request.setMovieId(initialRandomMovie.getId());
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
        assertThat(findReview.getMovie()).isEqualTo(initialRandomMovie);
    }

    @Test
    void 리뷰_수정() {
        //given
        Review reviewForEdit = Review.writeReview()
                .movie(initialRandomMovie)
                .comment("테스트")
                .writer("작성자")
                .score(10f)
                .build();

        reviewRepository.save(reviewForEdit);

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
        CreateReviewRequest request = new CreateReviewRequest();
        request.setMovieId(initialRandomMovie.getId());
        request.setWriter("작성자");
        request.setComment("내용");
        request.setScore(10f);

        PostIdResponse reviewForDelete = reviewService.write(request);

        // when
        reviewService.delete(reviewForDelete.getId());

        // then
        assertThat(reviewRepository.existsById(reviewForDelete.getId())).isFalse();
    }
}
