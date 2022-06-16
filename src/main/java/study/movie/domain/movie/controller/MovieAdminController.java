package study.movie.domain.movie.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

@Api(value = "Movie Admin Controller", tags = "[Admin] Movie")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/movies")
public class MovieAdminController {

    private final MovieService movieService;

    @Operation(summary = "영화 저장", description = "영화를 저장한다.")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid CreateMovieRequest request) {
        PostIdResponse result = movieService.save(request);
        return CustomResponse.success(CREATED, CREATE_MOVIE, result);
    }

    @Operation(summary = "영화 조건 검색", description = "조건으로 영화를 검색한다.")
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("search-type") MovieSearchType type, MovieSearchCond cond, PageableDTO pageableDTO) {
        Page<MovieSearchResponse> result = movieService.search(type, cond, pageableDTO);
        return CustomResponse.success(READ_MOVIE, result);
    }

    @Operation(summary = "영화 수정", description = "영화 id로 영화 조회 후 정보를 수정한다.")
    @Parameters({@Parameter(name = "id", description = "영화의 id", required = true, in = ParameterIn.PATH)})
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid UpdateMovieRequest request) {
        movieService.update(id, request);
        return CustomResponse.success(UPDATE_MOVIE);
    }

    @Operation(summary = "영화 삭제", description = "영화 id로 해당 영화를 삭제한다.")
    @Parameters({@Parameter(name = "id", description = "영화의 id", required = true)})
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody IdListRequest request) {
        movieService.delete(request);
        return CustomResponse.success(DELETE_MOVIE);
    }
}
