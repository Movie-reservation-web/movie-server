package study.movie.repository.movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.movie.domain.movie.Review;
import study.movie.dto.movie.condition.ReviewSearchCond;

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
