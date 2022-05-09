package study.movie.controller.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.dto.movie.*;
import study.movie.global.dto.CustomResponse;
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
    public ResponseEntity<?> save(@RequestBody @Valid CreateMovieRequest request){
        BasicMovieResponse result = movieService.saveMovie(request);
        return CustomResponse.success(CREATED,CREATE_MOVIE,result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody @Valid UpdateMovieRequest request) throws Exception {
        movieService.updateMovie(request);
        return CustomResponse.success(UPDATE_MOVIE);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) throws Exception {
        movieService.deleteMovie(id);
        return CustomResponse.success(DELETE_MOVIE);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findMovie(@PathVariable("id") Long id) throws Exception {
        BasicMovieResponse result = movieService.findOneMovie(id);
        return CustomResponse.success(READ_MOVIE,result);
    }

    @GetMapping("/search_condition")
    public ResponseEntity<?> searchMovie(@RequestBody @Valid MovieCondition condition) throws Exception {
        List<FindMovieResponse> result = movieService.findByCondition(condition);
        return CustomResponse.success(READ_MOVIE_CONDITION,result);
    }

    @GetMapping("/search_unreleased")
    public ResponseEntity<?> searchUnreleasedMovie() throws Exception {
        List<FindMovieResponse> result = movieService.findUnreleasedMovies();
        return CustomResponse.success(READ_UNRELEASED_MOVIE,result);
    }

    @GetMapping("/search_sort")
    public ResponseEntity<?> searchMovie(@RequestParam String orderCondition) throws Exception {
        List<FindMovieResponse> result = movieService.findByOrderBy(orderCondition);
        return CustomResponse.success(READ_MOVIE_SORT,result);
    }
}
