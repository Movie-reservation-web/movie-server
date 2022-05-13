package study.movie.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import study.movie.theater.entity.ScreenFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class ScheduleScreenRequest {

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate screenDate;

    @NotNull
    private Long movieId;

    @NotBlank
    private ScreenFormat screenFormat;

    @NotNull
    private List<Long> theaterIds;
}
