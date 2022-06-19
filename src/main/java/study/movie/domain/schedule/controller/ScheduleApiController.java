package study.movie.domain.schedule.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.domain.schedule.dto.condition.ScheduleBasicSearchCond;
import study.movie.global.dto.CustomResponse;
import study.movie.domain.schedule.dto.request.ReservationScreenRequest;
import study.movie.domain.schedule.dto.request.ScheduleScreenRequest;
import study.movie.domain.schedule.dto.response.MovieFormatResponse;
import study.movie.domain.schedule.dto.response.ReservationScreenResponse;
import study.movie.domain.schedule.dto.response.ScheduleScreenResponse;
import study.movie.domain.schedule.dto.response.ScheduleSearchResponse;
import study.movie.domain.schedule.service.ScheduleService;

import javax.validation.Valid;
import java.util.List;

import static study.movie.global.constants.ResponseMessage.*;

@Api(value = "Schedules Api Controller", tags = "[Api] Schedules")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
public class ScheduleApiController {
    private final ScheduleService scheduleService;

    @Operation(summary = "상영일정 리스트 조건 조회", description = "조건으로 상영일정(영화, 날짜, 극장) 리스트를 조회한다.")
    @PostMapping("/basic")
    public ResponseEntity<?> searchBasicSchedules(@RequestBody ScheduleBasicSearchCond cond) {
        List<ScheduleSearchResponse> result = scheduleService.searchBasicSchedules(cond);
        return CustomResponse.success(READ_SCHEDULE, result);
    }
    //??
    @Operation(summary = "상영일정(상영관) 조회", description = "상영일정 id로 상영관을 조회한다.")
    @PostMapping("/screen")
    public ResponseEntity<?> searchScheduleScreens(@Valid @RequestBody ScheduleScreenRequest request) {
        List<ScheduleScreenResponse> result = scheduleService.searchScheduleScreens(request);
        return CustomResponse.success(READ_SCHEDULE_SCREEN, result);
    }

    @Operation(summary = "영화포멧 조회", description = "상영일정 id로 해당 영화의 포멧을 조회한다.")
    @Parameters({@Parameter(name = "id", description = "상영일정 id", required = true, in = ParameterIn.PATH)})
    @GetMapping("/movie-format/{title}")
    public ResponseEntity<?> findMovieFormats(@PathVariable String title) {
        MovieFormatResponse result = scheduleService.searchScheduleByMovie(title);
        return CustomResponse.success(READ_MOVIE_FORMATS, result);
    }

    @Operation(summary = "상영일정(좌석 정보) 조회", description = "상영일정 id로 좌석 정보, 금액을 조회한다.")
    @Parameters({@Parameter(name = "id", description = "상영일정 id", required = true, in = ParameterIn.PATH)})
    @PostMapping("/seat-info/{id}")
    public ResponseEntity<?> getScheduleSeatInfo(@RequestParam Long id, @Valid @RequestBody ReservationScreenRequest request) {
        ReservationScreenResponse result = scheduleService.getSelectedScreenInfo(request);
        return CustomResponse.success(READ_SCHEDULE_SEATS, result);
    }
}
