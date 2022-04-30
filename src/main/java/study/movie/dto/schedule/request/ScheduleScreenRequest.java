package study.movie.dto.schedule.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import study.movie.domain.theater.ScreenFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ScheduleScreenRequest {

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate screenDate;

    @NotBlank
    private String movieTitle;

    @NotBlank
    private ScreenFormat screenFormat;

    @NotBlank
    private String theaterName;
}
