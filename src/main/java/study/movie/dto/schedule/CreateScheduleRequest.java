package study.movie.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CreateScheduleRequest {

    @Future
    private LocalDateTime startTime;
    private Long movieId;
    private Long screenId;
}
