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
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid CreateMovieRequest request){
        CreateMovieResponse result = movieService.saveMovie(request);
        return Response.success(CREATED,CREATE_MOVIE,result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody @Valid UpdateMovieRequest request) throws Exception {
        movieService.updateMovie(request);
        return Response.success(UPDATE_MOVIE);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) throws Exception {
        movieService.deleteMovie(id);
        return Response.success(DELETE_MOVIE);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findMovie(@PathVariable("id") Long id) throws Exception {
        CreateMovieResponse result = movieService.findOneMovie(id);
        return Response.success(READ_MOVIE,result);
    }

    @GetMapping("/search_condition")
    public ResponseEntity<?> searchMovie(@RequestBody @Valid MovieCondition condition) throws Exception {
        List<FindMovieResponse> result = movieService.findByCondition(condition);
        return Response.success(READ_MOVIE_CONDITION,result);
    }

    @GetMapping("/search_unreleased")
    public ResponseEntity<?> searchUnreleasedMovie() throws Exception {
        List<FindMovieResponse> result = movieService.findUnreleasedMovies();
        return Response.success(READ_UNRELEASED_MOVIE,result);
    }

    @GetMapping("/search_sort")
    public ResponseEntity<?> searchMovie(@RequestParam String orderCondition) throws Exception {
        List<FindMovieResponse> result = movieService.findByOrderBy(orderCondition);
        return Response.success(READ_MOVIE_SORT,result);
    }
}
