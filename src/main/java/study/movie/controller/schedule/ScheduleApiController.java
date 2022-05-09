package study.movie.controller.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.dto.schedule.condition.ScheduleBasicSearchCond;
import study.movie.dto.schedule.request.ReservationScreenRequest;
import study.movie.dto.schedule.request.ScheduleScreenRequest;
import study.movie.dto.schedule.response.*;
import study.movie.global.dto.CustomResponse;
import study.movie.service.schedule.ScheduleService;

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

    @GetMapping("/selected")
    public ResponseEntity<?> getScheduleSeatInfo(@Valid @RequestBody ReservationScreenRequest request) {
        ReservationScreenResponse result = scheduleService.getSelectedScreenInfo(request);
        return CustomResponse.success(READ_SCHEDULE_SEATS, result);
    }
}
