package study.movie.dto.schedule;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleSearchCond {

    private LocalDate screenDate;
    private String movieTitle;
    private String theaterName;
    private boolean finalSearch;

}

