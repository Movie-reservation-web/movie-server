package study.movie.movie.service;

import org.springframework.data.domain.Page;
import study.movie.movie.dto.request.CreateReviewRequest;
import study.movie.movie.dto.response.ReviewResponse;
import study.movie.movie.dto.request.UpdateReviewRequest;
import study.movie.movie.dto.condition.ReviewSearchCond;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.PageableDTO;

public interface ReviewService {

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


    /**
     * Admin Server
     * <p>
     * 해당 영화의 모든 리뷰 조회
     */
    Page<ReviewResponse> search(ReviewSearchCond cond, PageableDTO pageableDTO);
}
