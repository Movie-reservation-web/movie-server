package study.movie.repository.movie;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.Review;
import study.movie.dto.movie.condition.ReviewSearchCond;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
@Transactional
@Slf4j
class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    EntityManager em;
    @Autowired
    InitService init;

    @Test
    void 작성자로_리뷰_검색() {
        Movie movie1 = init.createMovie("영화1", "홍길동", Collections.singletonList(FilmFormat.TWO_D), Arrays.asList("배우1"));
        Movie movie2 = init.createMovie("영화2", "홍길동", Collections.singletonList(FilmFormat.TWO_D), Arrays.asList("배우1"));
        Review review1 = init.writeReview(movie1, "작성자1", 4f);
        Review review2 = init.writeReview(movie1, "작성자2", 5f);
        Review review3 = init.writeReview(movie2, "작성자3", 6f);
        Review review4 = init.writeReview(movie2, "작성자4", 8f);

        ReviewSearchCond cond = new ReviewSearchCond();
        cond.setWriter(review1.getWriter());

        Page<Review> result = reviewRepository.search(cond, PageRequest.of(0, 5));
        Assertions.assertThat(result.getContent()).containsExactly(review1);
    }

    @Test
    void 영화제목으로_리뷰_검색() {
        Movie movie1 = init.createMovie("영화1", "홍길동", Collections.singletonList(FilmFormat.TWO_D), Arrays.asList("배우1"));
        Movie movie2 = init.createMovie("영화2", "홍길동", Collections.singletonList(FilmFormat.TWO_D), Arrays.asList("배우1"));
        Review review1 = init.writeReview(movie1, "작성자1", 4f);
        Review review2 = init.writeReview(movie1, "작성자2", 5f);
        Review review3 = init.writeReview(movie2, "작성자3", 6f);
        Review review4 = init.writeReview(movie2, "작성자4", 8f);

        ReviewSearchCond cond = new ReviewSearchCond();
        cond.setMovieTitle(movie1.getTitle());

        Page<Review> result = reviewRepository.search(cond, PageRequest.of(0, 5));
        Assertions.assertThat(result.getContent()).containsExactly(review1, review2);
    }

    @Test
    void 리뷰_삭제_orphanRemoval_고아객체_삭제() throws Exception {
        // given
        Movie movie = init.createBasicMovie();
        Review savedReview = init.writeReview(movie);
        Long id = savedReview.getId();
        em.clear();

        // when
        System.out.println("============START DELETE REVIEW=============");

        Review findReview = reviewRepository.findById(id).get();
//        findReview.delete();

        em.flush(); // transactional 사용하기 위해
        System.out.println("============END DELETE REVIEW=============");
        // then
    }

    @Test
    void 리뷰_삭제_연관관계_제거_후_삭제() throws Exception {
        // given
        Movie movie = init.createBasicMovie();
        Review savedReview = init.writeReview(movie);
        Long id = savedReview.getId();
        em.clear();

        // when
        System.out.println("============START DELETE REVIEW=============");

        Review findReview = reviewRepository.findById(id).get();
//        findReview.delete();
        reviewRepository.delete(findReview);

        System.out.println("============END DELETE REVIEW=============");

        // then
    }

    @Test
    void 리뷰_삭제_Cascade_PERSIST() throws Exception {
        // given
        Movie movie = init.createBasicMovie();
        Review savedReview = init.writeReview(movie);
        Long id = savedReview.getId();
        em.clear();

        // when
        System.out.println("============START DELETE REVIEW=============");
        reviewRepository.deleteByIdEqQuery(id);
        System.out.println("============END DELETE REVIEW=============");
        // then
    }
}