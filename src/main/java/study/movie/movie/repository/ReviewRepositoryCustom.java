package study.movie.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.movie.movie.entity.Review;
import study.movie.movie.dto.condition.ReviewSearchCond;

public interface ReviewRepositoryCustom {

    /**
     * 리뷰 검색
     *
     * @param cond     writer, movieTitle
     * @param pageable id(asc,desc), score(asc,desc)
     * @return Page
     */
    Page<Review> search(ReviewSearchCond cond, Pageable pageable);
}
