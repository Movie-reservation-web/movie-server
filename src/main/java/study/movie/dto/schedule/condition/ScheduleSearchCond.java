package study.movie.dto.schedule.condition;

import lombok.Data;
import study.movie.domain.theater.ScreenFormat;

import java.time.LocalDate;

@Data
public class ScheduleSearchCond {

    private LocalDate screenDate;
    private String movieTitle;
    private ScreenFormat format;
    private String theaterName;
    private boolean finalSearch;
}

