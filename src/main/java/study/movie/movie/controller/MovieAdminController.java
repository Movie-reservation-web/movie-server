package study.movie.movie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.movie.dto.condition.MovieSearchCond;
import study.movie.movie.dto.condition.MovieSearchType;
import study.movie.movie.dto.request.CreateMovieRequest;
import study.movie.movie.dto.request.UpdateMovieRequest;
import study.movie.movie.dto.response.MovieSearchResponse;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.PageableDTO;
import study.movie.movie.service.MovieService;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static study.movie.global.constants.ResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/movies")
public class MovieAdminController {

    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid CreateMovieRequest request) {
        PostIdResponse result = movieService.save(request);
        return CustomResponse.success(CREATED, CREATE_MOVIE, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid UpdateMovieRequest request) {
        movieService.update(id, request);
        return CustomResponse.success(UPDATE_MOVIE);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        movieService.delete(id);
        return CustomResponse.success(DELETE_MOVIE);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("search-type") MovieSearchType type, MovieSearchCond cond, PageableDTO pageableDTO) {
        Page<MovieSearchResponse> result = movieService.search(type, cond, pageableDTO);
        return CustomResponse.success(READ_MOVIE, result);
    }
}
