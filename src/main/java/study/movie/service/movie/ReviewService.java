package study.movie.service.movie;

import org.springframework.data.domain.Page;
import study.movie.dto.movie.request.CreateReviewRequest;
import study.movie.dto.movie.response.ReviewResponse;
import study.movie.dto.movie.request.UpdateReviewRequest;
import study.movie.dto.movie.condition.ReviewSearchCond;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.PageableDTO;

public interface ReviewService {

    // API

    /**
     * Api Server
     * <p>
     * 리뷰 작성
     */
    PostIdResponse write(CreateReviewRequest request);

    /**
     * Api Server
     * <p>
     * 리뷰 수정
     */
    void edit(Long id, UpdateReviewRequest request);

    /**
     * Api Server
     * <p>
     * 리뷰 삭제
     */
    void delete(Long id);

    // ADMIN

    /**
     * Admin Server
     * 해당 영화의 모든 리뷰 조회
     */
    Page<ReviewResponse> search(ReviewSearchCond cond, PageableDTO pageableDTO);
}
