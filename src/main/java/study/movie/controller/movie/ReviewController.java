package study.movie.controller.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.dto.movie.CreateReviewRequest;
import study.movie.dto.movie.ReviewResponse;
import study.movie.dto.movie.UpdateReviewRequest;
import study.movie.global.dto.CustomResponse;
import study.movie.service.movie.MovieService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static study.movie.global.constants.ResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid CreateReviewRequest request) throws Exception {
        Long result = movieService.saveReview(request);
        return CustomResponse.success(CREATED,CREATE_REVIEW,result);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody @Valid UpdateReviewRequest request){
        movieService.updateReview(request);
        return CustomResponse.success(UPDATE_REVIEW);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) throws Exception {
        movieService.deleteReview(id);
        return CustomResponse.success(DELETE_REVIEW);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findAllReview(@PathVariable("id") Long id) throws Exception {
        List<ReviewResponse> result = movieService.findAllReviewByMovieId(id);
        return CustomResponse.success(READ_ALL_REVIEW,result);
    }
}
