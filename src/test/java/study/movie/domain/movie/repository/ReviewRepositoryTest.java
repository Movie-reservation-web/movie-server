package study.movie.domain.movie.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.dto.condition.ReviewSearchCond;
import study.movie.domain.movie.entity.Review;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    EntityManager em;

    private List<Review> initialReviewList;

    @BeforeEach
    void setUp() {
        initialReviewList = reviewRepository.findAll();
    }


    @Test
    @DisplayName("리뷰를 조건(작성자) 검색한다.")
    void 리뷰_조건_검색_작성자() {
        //given
        //when
        String writerForFind = initialReviewList.get(0).getWriter();
        List<Review> findByWriter = initialReviewList.stream()
                .filter(review -> review.getWriter().equals(writerForFind))
                .collect(Collectors.toList());

        ReviewSearchCond cond = new ReviewSearchCond();
        cond.setWriter(writerForFind);
        int pageSize = findByWriter.size();

        List<Review> foundReviewList = reviewRepository.search(cond, PageRequest.of(0, pageSize)).getContent();

        //then
        assertThat(foundReviewList.size()).isEqualTo(findByWriter.size());
        for (Review review : foundReviewList) {
            assertThat(review.getWriter()).isEqualTo(writerForFind);
        }
    }

    @Test
    @DisplayName("리뷰를 조건(영화 제목) 검색한다.")
    void 리뷰_조건_검색_영화제목() {
        //given
        //when
        String movieTitleForFind = initialReviewList.get(0).getMovie().getTitle();
        List<Review> findByMovieTitle = initialReviewList.stream()
                .filter(review -> review.getMovie().getTitle().equals(movieTitleForFind))
                .collect(Collectors.toList());

        ReviewSearchCond cond = new ReviewSearchCond();
        cond.setMovieTitle(movieTitleForFind);
        int pageSize = findByMovieTitle.size();

        List<Review> foundReviewList = reviewRepository.search(cond, PageRequest.of(0, pageSize)).getContent();

        //then
        assertThat(foundReviewList.size()).isEqualTo(findByMovieTitle.size());
        for (Review review : foundReviewList) {
            assertThat(review.getMovie().getTitle()).isEqualTo(movieTitleForFind);
        }
    }

    @Test
    @DisplayName("리뷰를 조건(작성자, 영화 제목) 검색한다.")
    void 리뷰_조건_검색_작성자_영화제목() {
        //given
        //when
        String movieTitleForFind = initialReviewList.get(0).getMovie().getTitle();
        String writerForFind = initialReviewList.get(0).getWriter();
        List<Review> findByCondition = initialReviewList.stream()
                .filter(review -> review.getMovie().getTitle().equals(movieTitleForFind))
                .filter(review -> review.getWriter().equals(writerForFind))
                .collect(Collectors.toList());

        ReviewSearchCond cond = new ReviewSearchCond();
        cond.setMovieTitle(movieTitleForFind);
        cond.setWriter(writerForFind);
        int pageSize = findByCondition.size();

        List<Review> foundReviewList = reviewRepository.search(cond, PageRequest.of(0, pageSize)).getContent();

        //then
        assertThat(foundReviewList.size()).isEqualTo(findByCondition.size());
        for (Review review : foundReviewList) {
            assertThat(review.getMovie().getTitle()).isEqualTo(movieTitleForFind);
            assertThat(review.getWriter()).isEqualTo(writerForFind);
        }
    }

//    @Test
//    void 리뷰_삭제_orphanRemoval_고아객체_삭제() throws Exception {
//        // given
//        Movie movie = init.createBasicMovie();
//        Review savedReview = init.writeReview(movie);
//        Long id = savedReview.getId();
//        em.clear();
//
//        // when
//        System.out.println("============START DELETE REVIEW=============");
//
//        Review findReview = reviewRepository.findById(id).get();
////        findReview.delete();
//
//        em.flush(); // transactional 사용하기 위해
//        System.out.println("============END DELETE REVIEW=============");
//        // then
//    }

//    @Test
//    void 리뷰_삭제_연관관계_제거_후_삭제() throws Exception {
//        // given
//        Movie movie = init.createBasicMovie();
//        Review savedReview = init.writeReview(movie);
//        Long id = savedReview.getId();
//        em.clear();
//
//        // when
//        System.out.println("============START DELETE REVIEW=============");
//
//        Review findReview = reviewRepository.findById(id).get();
////        findReview.delete();
//        reviewRepository.delete(findReview);
//
//        System.out.println("============END DELETE REVIEW=============");
//
//        // then
//    }

    @Test
    @DisplayName("리뷰를 삭제한다.")
    void 리뷰_삭제_Cascade_PERSIST(){
        // given
        Review reviewForDelete = initialReviewList.get(0);

        // when
        reviewRepository.deleteByIdEqQuery(reviewForDelete.getId());

        // then
        assertThat(reviewRepository.existsById(reviewForDelete.getId())).isFalse();
    }
}
