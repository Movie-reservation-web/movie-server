package study.movie.dto.schedule.condition;

import lombok.Data;
import study.movie.domain.schedule.ScheduleStatus;
import study.movie.domain.theater.ScreenFormat;
import study.movie.global.dto.DateRangeCond;

@Data
public class ScheduleSearchCond extends DateRangeCond {

    private String theaterName;
    private String movieTitle;
    private ScreenFormat screenFormat;
    private ScheduleStatus status;
}
