package study.movie.domain.movie.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.repository.MovieRepository;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static study.movie.global.utils.NumberUtil.getRandomIndex;

@SpringBootTest
@Transactional
@Slf4j
public class ReviewTest {

    @Autowired
    EntityManager em;

    @Autowired
    MovieRepository movieRepository;

    private Movie initialRandomMovie;

    @BeforeEach
    void setUp() {
        initialRandomMovie = movieRepository.findAll()
                .get((int) getRandomIndex(movieRepository.count()));
    }

    @Test
    void 리뷰_연관관계_메서드() {
        // given
        Review review = Review.writeReview()
                .comment("내용")
                .score(7f)
                .writer("작성자")
                .movie(initialRandomMovie)
                .build();
        em.flush();
        em.clear();

        // when
        Review findReview = em.find(Review.class, review.getId());

        // then
        assertThat(findReview.getMovie().toString()).isEqualTo(initialRandomMovie.toString());
    }

    @Test
    void 리뷰_비즈니스_로직_수정하기() {
        // given
        Review review = Review.writeReview()
                .comment("내용")
                .score(7f)
                .writer("작성자")
                .movie(initialRandomMovie)
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
