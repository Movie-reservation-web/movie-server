package study.movie.repository.schedule;

import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.SeatEntity;
import study.movie.domain.theater.ScreenFormat;
import study.movie.dto.schedule.ScheduleSearchCond;
import study.movie.dto.schedule.UpdateSeatCond;

import java.util.List;

public interface ScheduleRepositoryCustom {
    List<Schedule> searchSchedules(ScheduleSearchCond cond);

    void updateSeatStatus(UpdateSeatCond cond);

    List<SeatEntity> findSeatByScheduleId(Long scheduleId);

    List<ScreenFormat> findFormatByMovie(String movieTitle);
}
