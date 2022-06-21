package study.movie.domain.schedule.dto.condition;

import lombok.Getter;
import lombok.Setter;
import study.movie.domain.schedule.entity.ScheduleStatus;
import study.movie.domain.theater.entity.ScreenFormat;
import study.movie.global.dto.DateRangeCond;

@Getter
@Setter
public class ScheduleSearchCond extends DateRangeCond {

    private String theaterName;
    private String movieTitle;
    private ScreenFormat screenFormat;
    private ScheduleStatus status;
}
