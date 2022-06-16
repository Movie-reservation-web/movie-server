package study.movie.domain.movie.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.domain.movie.dto.request.CreateReviewRequest;
import study.movie.domain.movie.dto.response.ReviewResponse;
import study.movie.domain.movie.dto.request.UpdateReviewRequest;
import study.movie.domain.movie.dto.condition.ReviewSearchCond;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.PageableDTO;
import study.movie.domain.movie.service.ReviewService;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static study.movie.global.constants.ResponseMessage.*;

@Api(value = "Review Api Controller", tags = "[Api] Review")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewApiController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 저장", description = "리뷰를 저장한다.")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid CreateReviewRequest request) throws Exception {
        PostIdResponse result = reviewService.write(request);
        return CustomResponse.success(CREATED, CREATE_REVIEW, result);
    }

    @Operation(summary = "리뷰 수정", description = "리뷰 id로 리뷰를 수정한다.")
    @Parameters({@Parameter(name = "id", description = "리뷰의 id", required = true, in = ParameterIn.PATH)})
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid UpdateReviewRequest request) {
        reviewService.edit(id, request);
        return CustomResponse.success(UPDATE_REVIEW);
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰 id로 리뷰를 삭제한다.")
    @Parameters({@Parameter(name = "id", description = "리뷰의 id", required = true, in = ParameterIn.PATH)})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
        reviewService.delete(id);
        return CustomResponse.success(DELETE_REVIEW);
    }

    @Operation(summary = "리뷰 조건 검색", description = "조건(작성자, 영화제목), 페이지 정보로 리뷰를 조회한다.")
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestBody ReviewSearchCond cond, PageableDTO pageableDto) {
        Page<ReviewResponse> result = reviewService.search(cond, pageableDto);
        return CustomResponse.success(READ_REVIEW, result);
    }
}
