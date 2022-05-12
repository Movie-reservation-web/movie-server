package study.movie.controller.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.movie.dto.movie.condition.ReviewSearchCond;
import study.movie.dto.movie.response.ReviewResponse;
import study.movie.global.dto.CustomResponse;
import study.movie.global.paging.PageableDTO;
import study.movie.service.movie.ReviewService;

import static study.movie.global.constants.ResponseMessage.READ_REVIEW;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/reviews")
public class ReviewAdminController {

    private final ReviewService reviewService;

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestBody ReviewSearchCond cond, PageableDTO pageableDto) {
        Page<ReviewResponse> result = reviewService.search(cond, pageableDto);
        return CustomResponse.success(READ_REVIEW, result);
    }
}
