package study.movie.domain.movie.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.movie.domain.movie.dto.condition.ReviewSearchCond;
import study.movie.domain.movie.dto.response.ReviewResponse;
import study.movie.global.dto.CustomResponse;
import study.movie.global.paging.PageableDTO;
import study.movie.domain.movie.service.ReviewService;

import static study.movie.global.constants.ResponseMessage.READ_REVIEW;

@Api(value = "Review Admin Controller", tags = "[Admin] Review")
@RequiredArgsConstructor
@RequestMapping("/admin/v1/reviews")
public class ReviewAdminController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 조건 검색", description = "조건(작성자, 영화제목), 페이지 정보로 리뷰를 조회한다.")
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestBody ReviewSearchCond cond, PageableDTO pageableDto) {
        Page<ReviewResponse> result = reviewService.search(cond, pageableDto);
        return CustomResponse.success(READ_REVIEW, result);
    }
}
