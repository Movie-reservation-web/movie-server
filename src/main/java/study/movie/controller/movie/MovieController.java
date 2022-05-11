package study.movie.controller.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.dto.movie.*;
import study.movie.dto.schedule.response.MovieChartResponse;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.service.movie.MovieService;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static study.movie.global.constants.ResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid CreateMovieRequest request) {
        PostIdResponse result = movieService.saveMovie(request);
        return CustomResponse.success(CREATED, CREATE_MOVIE, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody @Valid UpdateMovieRequest request) {
        movieService.updateMovie(request);
        return CustomResponse.success(UPDATE_MOVIE);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        movieService.deleteMovie(id);
        return CustomResponse.success(DELETE_MOVIE);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findMovie(@PathVariable("id") Long id) {
        BasicMovieResponse result = movieService.findOneMovie(id);
        return CustomResponse.success(READ_MOVIE, result);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMovieByPerson(
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String actor) {
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
            @RequestParam MovieSortType sortType,
            @RequestParam(required = false) boolean isReleased) {
        List<MovieChartResponse> result = movieService.findMovieBySort(sortType, isReleased);
        return CustomResponse.success(READ_MOVIE_SORT, result);
    }
}
