package study.movie.domain.schedule.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

@Api(value = "Schedules Admin Controller", tags = "[Admin] Schedules")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/schedules")
public class ScheduleAdminController {
    private final ScheduleService scheduleService;

    @Operation(summary = "상영일정 저장", description = "상영일정을 저장한다.")
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CreateScheduleRequest request) {
        PostIdResponse result = scheduleService.save(request);
        return CustomResponse.success(CREATED, CREATE_SCHEDULE, result);
    }

    @Operation(summary = "상영일정 조건 검색", description = "조건(상영관이름, 영화제목, 상영타입, 상태)으로 상영일정 검색한다.")
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestBody(required = false) ScheduleSearchCond cond, PageableDTO pageableDTO) {
        Page<ScheduleResponse> result = scheduleService.search(cond, pageableDTO);
        return CustomResponse.success(READ_SCHEDULE, result);
    }

    @Operation(summary = "상영일정 조회", description = "상영일정 id로 상영일정을 조회한다.")
    @Parameters({@Parameter(name = "id", description = "상영일정 id", required = true, in = ParameterIn.PATH)})
    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id){
        ScheduleResponse result = scheduleService.findById(id);
        return CustomResponse.success(READ_SCHEDULE, result);
    }
}
