package study.movie.domain.schedule.controller;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
public class ScheduleApiController {
    private final ScheduleService scheduleService;

    @GetMapping("/basic")
    public ResponseEntity<?> searchBasicSchedules(@RequestBody ScheduleBasicSearchCond cond) {
        List<ScheduleSearchResponse> result = scheduleService.searchBasicSchedules(cond);
        return CustomResponse.success(READ_SCHEDULE, result);
    }

    @GetMapping("/screen")
    public ResponseEntity<?> searchScheduleScreens(@Valid @RequestBody ScheduleScreenRequest request) {
        List<ScheduleScreenResponse> result = scheduleService.searchScheduleScreens(request);
        return CustomResponse.success(READ_SCHEDULE_SCREEN, result);
    }

    @GetMapping("/movie-format/{title}")
    public ResponseEntity<?> findMovieFormats(@PathVariable String title) {
        MovieFormatResponse result = scheduleService.searchScheduleByMovie(title);
        return CustomResponse.success(READ_MOVIE_FORMATS, result);
    }

    @GetMapping("/seat-info/{id}")
    public ResponseEntity<?> getScheduleSeatInfo(@RequestParam Long id, @Valid @RequestBody ReservationScreenRequest request) {
        ReservationScreenResponse result = scheduleService.getSelectedScreenInfo(request);
        return CustomResponse.success(READ_SCHEDULE_SEATS, result);
    }
}
