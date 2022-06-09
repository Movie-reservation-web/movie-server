package study.movie.domain.movie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.domain.movie.dto.condition.MovieSearchCond;
import study.movie.domain.movie.dto.condition.MovieSearchType;
import study.movie.domain.movie.dto.request.CreateMovieRequest;
import study.movie.domain.movie.dto.request.UpdateMovieRequest;
import study.movie.domain.movie.dto.response.MovieSearchResponse;
import study.movie.domain.movie.service.MovieService;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.IdListRequest;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.PageableDTO;

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

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("search-type") MovieSearchType type, MovieSearchCond cond, PageableDTO pageableDTO) {
        Page<MovieSearchResponse> result = movieService.search(type, cond, pageableDTO);
        return CustomResponse.success(READ_MOVIE, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid UpdateMovieRequest request) {
        movieService.update(id, request);
        return CustomResponse.success(UPDATE_MOVIE);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody IdListRequest request) {
        movieService.delete(request);
        return CustomResponse.success(DELETE_MOVIE);
    }
}
