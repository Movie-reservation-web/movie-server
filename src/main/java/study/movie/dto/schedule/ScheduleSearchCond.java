package study.movie.dto.schedule;

import lombok.Data;
import study.movie.domain.movie.FilmFormat;

import java.time.LocalDate;

@Data
public class ScheduleSearchCond {

    private LocalDate screenDate;
    private String movieTitle;
    private FilmFormat format;
    private String theaterName;
    private boolean finalSearch;
}

