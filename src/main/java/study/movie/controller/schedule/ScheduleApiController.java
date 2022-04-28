package study.movie.controller.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.dto.schedule.condition.ScheduleBasicSearchCond;
import study.movie.dto.schedule.request.ScheduleScreenRequest;
import study.movie.dto.schedule.response.MovieFormatResponse;
import study.movie.dto.schedule.response.ScheduleScreenResponse;
import study.movie.dto.schedule.response.ScheduleSearchResponse;
import study.movie.dto.schedule.response.SeatResponse;
import study.movie.global.dto.Response;
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
        return Response.success(READ_SCHEDULE, result);
    }
    @GetMapping("/screen")
    public ResponseEntity<?> searchScheduleScreens(@Valid @RequestBody ScheduleScreenRequest request) {
        List<ScheduleScreenResponse> result = scheduleService.searchScheduleScreens(request);
        return Response.success(READ_SCHEDULE_SCREEN, result);
    }

    @GetMapping("/movie-format/{title}")
    public ResponseEntity<?> findMovieFormats(@PathVariable String title) {
        MovieFormatResponse result = scheduleService.searchScheduleByMovie(title);
        return Response.success(READ_MOVIE_FORMATS, result);
    }

    @GetMapping("/seats/{id}")
    public ResponseEntity<?> getScheduleSeatInfo(@PathVariable Long id) {
        List<SeatResponse> result = scheduleService.getScheduleSeatEntity(id);
        return Response.success(READ_SCHEDULE_SEATS, result);
    }
}
