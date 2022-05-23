package study.movie.domain.movie.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.global.dto.PostIdResponse;
import study.movie.domain.movie.dto.request.CreateReviewRequest;
import study.movie.domain.movie.dto.request.UpdateReviewRequest;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.movie.entity.Review;
import study.movie.domain.movie.repository.ReviewRepository;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
public class ReviewServiceTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ReviewService reviewService;

    @Autowired
    EntityManager em;

    @Autowired
    InitService init;


    @Test
    void 리뷰_작성() {
        //given
        Movie movie = init.createBasicMovie();

        CreateReviewRequest request = new CreateReviewRequest();
        request.setMovieId(movie.getId());
        request.setWriter("작성자");
        request.setComment("내용");
        request.setScore(10f);

        // when
        PostIdResponse response = reviewService.write(request);
        System.out.println("response = " + response);

        Review findReview = reviewRepository.findById(response.getId()).get();

        //when
        assertThat(findReview.getScore()).isEqualTo(request.getScore());
        assertThat(findReview.getWriter()).isEqualTo(request.getWriter());
        assertThat(findReview.getComment()).isEqualTo(request.getComment());
        assertThat(findReview.getMovie()).isEqualTo(movie);
    }

    @Test
    void 리뷰_수정() {
        //given
        Movie movie = init.createBasicMovie();
        Review review = Review.writeReview()
                .comment("내용")
                .score(7f)
                .writer("작성자")
                .movie(movie)
                .build();
        em.flush();

        // when
        UpdateReviewRequest request = new UpdateReviewRequest();
        request.setComment("내용1");
        request.setScore(10f);

        reviewService.edit(review.getId(), request);
        em.flush();

        Review findReview = reviewRepository.findById(review.getId()).get();

        //when
        assertThat(findReview.getScore()).isEqualTo(request.getScore());
        assertThat(findReview.getComment()).isEqualTo(request.getComment());
    }

    @Test
    void 리뷰_삭제() {
        // given
        Movie movie = init.createBasicMovie();
        Review review1 = Review.writeReview()
                .comment("내용1")
                .score(7f)
                .writer("작성자1")
                .movie(movie)
                .build();

        Review review2 = Review.writeReview()
                .comment("내용2")
                .score(8f)
                .writer("작성자2")
                .movie(movie)
                .build();
        em.flush();

        // when
        reviewService.delete(review1.getId());

        // then
        assertThat(reviewRepository.count()).isEqualTo(1);
    }
}
