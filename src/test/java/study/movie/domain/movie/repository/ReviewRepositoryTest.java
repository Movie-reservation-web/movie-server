package study.movie.domain.movie.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.Review;
import study.movie.repository.movie.ReviewRepository;

@SpringBootTest
@Transactional
class ReviewRepositoryTest {
    @Autowired
    ReviewRepository reviewRepository;

    @Test
    @DisplayName("리뷰를 저장하고 조회한다.")
    public void 리뷰_저장_조회() throws Exception {
        // given
        Review review = Review.builder()
                .comment("review comment")
                .score(9.56f)
                .writer("writer1")
                .build();
        Review savedReview = reviewRepository.save(review);

        // when
        Review findReview = reviewRepository.findById(savedReview.getId()).orElse(null);

        // then
        Assertions.assertEquals(savedReview, findReview);
    }

}