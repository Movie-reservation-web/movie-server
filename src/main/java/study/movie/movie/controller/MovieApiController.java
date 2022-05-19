package study.movie.movie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.movie.dto.condition.MovieChartSortType;
import study.movie.movie.dto.response.BasicMovieResponse;
import study.movie.movie.dto.response.FindMovieResponse;
import study.movie.schedule.dto.response.MovieChartResponse;
import study.movie.global.dto.CustomResponse;
import study.movie.exception.CustomException;
import study.movie.movie.service.MovieService;

import java.util.List;

import static study.movie.global.constants.ResponseMessage.*;
import static study.movie.exception.ErrorCode.ILLEGAL_ARGUMENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movies")
public class MovieApiController {

    private final MovieService movieService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findMovie(@PathVariable("id") Long id) {
        BasicMovieResponse result = movieService.findOneMovie(id);
        return CustomResponse.success(READ_MOVIE, result);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMovieByPerson(
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String actor) {
        if (director == null && actor == null) throw new CustomException(ILLEGAL_ARGUMENT);
        List<FindMovieResponse> result =
                director != null
                        ? movieService.findMovieByDirector(director)
                        : movieService.findMovieByActor(actor);
        return CustomResponse.success(READ_MOVIE_CONDITION, result);
    }

    @GetMapping("/chart/unreleased")
    public ResponseEntity<?> getUnreleasedMovieChart() {
        List<MovieChartResponse> result = movieService.findUnreleasedMovies();
        return CustomResponse.success(READ_UNRELEASED_MOVIE, result);
    }

    @GetMapping("/chart")
    public ResponseEntity<?> getMoveChart(
            @RequestParam MovieChartSortType sortType,
            @RequestParam(required = false) boolean isReleased) {
        List<MovieChartResponse> result = movieService.findMovieBySort(sortType, isReleased);
        return CustomResponse.success(READ_MOVIE_SORT, result);
    }
}
