package study.movie.domain.movie.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
public class ReviewTest {

    @Autowired
    EntityManager em;
    @Autowired
    InitService init;

    @Test
    void 리뷰_연관관계_메서드() {
        // given
        Movie movie = em.find(Movie.class, 1L);

        Review review = Review.writeReview()
                .comment("내용")
                .score(7f)
                .writer("작성자")
                .movie(movie)
                .build();
        em.flush();
        em.clear();

        // when
        Review findReview = em.find(Review.class, review.getId());

        // then
        assertThat(movie.getReviews()).containsExactly(findReview);
        assertThat(findReview.getMovie()).isEqualTo(movie);
    }

    @Test
    void 리뷰_비즈니스_로직_수정하기() {
        // given
        Movie movie = em.find(Movie.class, 1L);

        Review review = Review.writeReview()
                .comment("내용")
                .score(7f)
                .writer("작성자")
                .movie(movie)
                .build();

        // when
        String editComment = "리뷰 내용";
        float editScore = 8f;
        review.edit(editScore, editComment);
        em.flush();
        em.clear();

        Review editReview = em.find(Review.class, review.getId());

        //then
        assertThat(editReview.getComment()).isEqualTo(editComment);
        assertThat(editReview.getScore()).isEqualTo(editScore);

    }
}
