package study.movie.dto.schedule.condition;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import study.movie.domain.schedule.ScheduleStatus;
import study.movie.domain.theater.ScreenFormat;

import java.time.LocalDate;

@Data
public class ScheduleSearchCond {

    private String theaterName;
    private String movieTitle;
    private ScreenFormat screenFormat;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate screenStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate screenEndDate;

    private ScheduleStatus status;
}
