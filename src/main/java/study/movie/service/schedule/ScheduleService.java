package study.movie.service.schedule;

import study.movie.dto.schedule.condition.ScheduleSearchCond;
import study.movie.dto.schedule.request.CreateScheduleRequest;
import study.movie.dto.schedule.response.*;

import java.util.List;

public interface ScheduleService {
    CreateScheduleResponse save(CreateScheduleRequest request);

    List<ScheduleSearchResponse> findAllSchedules();

    List<? extends BaseScheduleResponse> searchSchedules(ScheduleSearchCond cond);

    MovieFormatResponse searchScheduleByMovie(String movieTitle);

    List<SeatResponse> getScheduleSeatEntity(Long scheduleId);

    void removeSchedule(Long id);
}
