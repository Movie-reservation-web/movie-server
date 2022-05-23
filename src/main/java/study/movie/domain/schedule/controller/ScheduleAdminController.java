package study.movie.domain.schedule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.domain.schedule.dto.request.CreateScheduleRequest;
import study.movie.domain.schedule.service.ScheduleService;
import study.movie.domain.schedule.dto.condition.ScheduleSearchCond;
import study.movie.domain.schedule.dto.response.ScheduleResponse;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.PageableDTO;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static study.movie.global.constants.ResponseMessage.CREATE_SCHEDULE;
import static study.movie.global.constants.ResponseMessage.READ_SCHEDULE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/schedules")
public class ScheduleAdminController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CreateScheduleRequest request) {
        PostIdResponse result = scheduleService.save(request);
        return CustomResponse.success(CREATED, CREATE_SCHEDULE, result);
    }

    @GetMapping("/search")
    public ResponseEntity<?> findAll(@RequestBody(required = false) ScheduleSearchCond cond, PageableDTO pageableDTO) {
        Page<ScheduleResponse> result = scheduleService.search(cond, pageableDTO);
        return CustomResponse.success(READ_SCHEDULE, result);
    }
}
