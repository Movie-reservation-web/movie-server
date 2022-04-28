package study.movie.dto.schedule.condition;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import study.movie.domain.schedule.ScheduleStatus;
import study.movie.domain.theater.ScreenFormat;

import java.time.LocalDate;

@Data
public class ScheduleSearchCond {

    private String movieTitle;
    private String movieNation;

    private String theaterName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate screenDate;
    private ScreenFormat screenFormat;

    private ScheduleStatus scheduleStatus;
    private String scheduleNumber;
}
