package study.movie.controller.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.dto.movie.request.CreateReviewRequest;
import study.movie.dto.movie.response.ReviewResponse;
import study.movie.dto.movie.request.UpdateReviewRequest;
import study.movie.dto.movie.condition.ReviewSearchCond;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.PageableDTO;
import study.movie.service.movie.ReviewService;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static study.movie.global.constants.ResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid CreateReviewRequest request) throws Exception {
        PostIdResponse result = reviewService.write(request);
        return CustomResponse.success(CREATED, CREATE_REVIEW, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid UpdateReviewRequest request) {
        reviewService.edit(id, request);
        return CustomResponse.success(UPDATE_REVIEW);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
        reviewService.delete(id);
        return CustomResponse.success(DELETE_REVIEW);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestBody ReviewSearchCond cond, PageableDTO pageableDto) {
        Page<ReviewResponse> result = reviewService.search(cond, pageableDto);
        return CustomResponse.success(READ_REVIEW, result);
    }
}
