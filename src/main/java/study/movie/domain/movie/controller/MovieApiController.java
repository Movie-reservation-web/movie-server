package study.movie.domain.movie.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.domain.movie.dto.condition.MovieChartSortType;
import study.movie.domain.movie.dto.response.BasicMovieResponse;
import study.movie.domain.movie.dto.response.FindMovieResponse;
import study.movie.domain.movie.service.MovieService;
import study.movie.domain.schedule.dto.response.MovieChartResponse;
import study.movie.global.dto.CustomResponse;
import study.movie.exception.CustomException;

import java.util.List;

import static study.movie.global.constants.ResponseMessage.*;
import static study.movie.exception.ErrorCode.ILLEGAL_ARGUMENT;

@Api(value = "Movie Api Controller", tags = "[Api] Movie")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movies")
public class MovieApiController {

    private final MovieService movieService;

    @Operation(summary = "영화 조회", description = "영화 id로 영화를 조회한다.")
    @Parameters({@Parameter(name = "id", description = "영화의 id", required = true, in = ParameterIn.PATH)})
    @GetMapping("/{id}")
    public ResponseEntity<?> findMovie(@PathVariable("id") Long id) {
        BasicMovieResponse result = movieService.findOneMovie(id);
        return CustomResponse.success(READ_MOVIE, result);
    }

    @Operation(summary = "영화 조건 검색", description = "감독, 배우 조건으로 영화를 검색한다.")
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

    @Operation(summary = "상영예정 영화 조회", description = "상영예정인 영화들을 조회한다.")
    @GetMapping("/chart/unreleased")
    public ResponseEntity<?> getUnreleasedMovieChart() {
        List<MovieChartResponse> result = movieService.findUnreleasedMovies();
        return CustomResponse.success(READ_UNRELEASED_MOVIE, result);
    }

    @Operation(summary = "영화 차트 보기", description = "정렬조건(관객순, 평점순), 탐색조건(상영예정여부)으로 영화 리스트를 출력한다.")
    @GetMapping("/chart")
    public ResponseEntity<?> getMoveChart(
            @RequestParam String sort,
            @RequestParam boolean isReleased) {
        List<MovieChartResponse> result = movieService.findMovieBySort(MovieChartSortType.getSortType(sort), isReleased);
        return CustomResponse.success(READ_MOVIE_SORT, result);
    }

    @Operation(summary = "영화 관람객 수 수정", description = "하루의 영화 관람객 수를 업데이트 한다.")
    @GetMapping("/update")
    public ResponseEntity<?> update() {
        movieService.updateMovieAudience();
        return CustomResponse.success(UPDATE_MOVIE);
    }
}
