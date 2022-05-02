package study.movie.dto.schedule.condition;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import study.movie.domain.theater.ScreenFormat;

import java.time.LocalDate;

@Data
public class ScheduleBasicSearchCond {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate screenDate;

    private String movieTitle;

    private ScreenFormat screenFormat;

    private String theaterName;
}

