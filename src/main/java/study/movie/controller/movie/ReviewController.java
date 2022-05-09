package study.movie.controller.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.dto.movie.*;
import study.movie.global.dto.Response;
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
        return Response.success(CREATED,CREATE_REVIEW,result);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody @Valid UpdateReviewRequest request) throws Exception {
        movieService.updateReview(request);
        return Response.success(UPDATE_REVIEW);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) throws Exception {
        movieService.deleteReview(id);
        return Response.success(DELETE_REVIEW);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findAllReview(@PathVariable("id") Long id) throws Exception {
        List<ReviewResponse> result = movieService.findAllReviewByMovieId(id);
        return Response.success(READ_ALL_REVIEW,result);
    }
}
