package study.movie.service.schedule;

import study.movie.dto.schedule.*;

import java.util.List;

public interface ScheduleService {
    CreateScheduleResponse save(CreateScheduleRequest request);

    List<ScheduleSearchResponse> findAllSchedules();

    List<? extends BaseScheduleResponse> searchSchedules(ScheduleSearchCond cond);

    void removeSchedule(Long id);
}
