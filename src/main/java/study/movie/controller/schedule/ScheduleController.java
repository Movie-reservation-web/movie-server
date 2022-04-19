package study.movie.controller.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.dto.schedule.condition.ScheduleSearchCond;
import study.movie.dto.schedule.request.CreateScheduleRequest;
import study.movie.dto.schedule.response.*;
import study.movie.global.dto.Response;
import study.movie.service.schedule.ScheduleService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static study.movie.global.constants.ResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<?> saveSchedule(@Valid @RequestBody CreateScheduleRequest request) {
        CreateScheduleResponse result = scheduleService.save(request);
        return Response.success(CREATED,CREATE_SCHEDULE, result);
    }

    @GetMapping
    public ResponseEntity<?> searchSchedules(@RequestBody ScheduleSearchCond cond) {
        List<? extends BaseScheduleResponse> result = scheduleService.searchSchedules(cond);
        return result instanceof ScheduleScreenResponse
                ? Response.success(READ_SCHEDULE_SCREEN, result)
                : Response.success(READ_SCHEDULE, result);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeSchedule(@PathVariable Long id) {
        scheduleService.removeSchedule(id);
        return Response.success(DELETE_SCHEDULE);
    }
}
