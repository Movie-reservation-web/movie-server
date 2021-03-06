package study.movie.domain.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import study.movie.domain.theater.entity.ScreenFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ReservationScreenRequest {

    @NotNull
    private Long id;
    @NotNull
    private ScreenFormat format;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;
}
