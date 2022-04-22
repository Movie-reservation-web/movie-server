package study.movie.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.Review;
import study.movie.repository.movie.ReviewRepository;

import javax.persistence.EntityManager;
import java.util.Arrays;

@SpringBootTest
@Transactional
@Slf4j
@Rollback
class MovieServiceTest {
    @Autowired
    EntityManager em;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    InitService init;

    @Test
    void 리뷰_삭제_orphanRemoval_고아객체_삭제() throws Exception {
        // given
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        Review savedReview = init.writeReview(movie);
        Long id = savedReview.getId();
        em.clear();

        // when
        System.out.println("============START DELETE REVIEW=============");

        Review findReview = reviewRepository.findById(id).get();
        findReview.delete();

        em.flush(); // transactional 사용하기 위해
        System.out.println("============END DELETE REVIEW=============");
        // then
    }

    @Test
    void 리뷰_삭제_연관관계_제거_후_삭제() throws Exception {
        // given
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
        Review savedReview = init.writeReview(movie);
        Long id = savedReview.getId();
        em.clear();

        // when
        System.out.println("============START DELETE REVIEW=============");

        Review findReview = reviewRepository.findById(id).get();
        findReview.delete();
        reviewRepository.delete(findReview);

        System.out.println("============END DELETE REVIEW=============");

        // then
    }

    @Test
    void 리뷰_삭제_Cascade_PERSIST() throws Exception {
        // given
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
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