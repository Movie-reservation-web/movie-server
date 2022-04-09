package study.movie.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CreateScheduleRequest {

    private LocalDateTime startTime;
    private Long movieId;
    private Long screenId;
}
