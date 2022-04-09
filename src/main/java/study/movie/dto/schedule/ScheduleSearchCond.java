package study.movie.dto.schedule;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ScheduleSearchCond {

    private LocalDate screenDate;
    private String movieTitle;
    private List<String> formats;
    private String theaterName;
    private boolean finalSearch;

}

