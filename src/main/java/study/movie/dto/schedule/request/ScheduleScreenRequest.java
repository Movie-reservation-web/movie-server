package study.movie.dto.schedule.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import study.movie.domain.theater.ScreenFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class ScheduleScreenRequest {

    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate screenDate;

    @NotBlank
    private String movieTitle;

    @NotBlank
    private ScreenFormat screenFormat;

    @NotBlank
    private String theaterName;
}
