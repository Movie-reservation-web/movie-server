package study.movie.dto.schedule;

import lombok.Data;
import study.movie.domain.schedule.Schedule;

import java.time.LocalDateTime;

@Data
public class CreateScheduleResponse {

    private String movieTitle;
    private String theaterName;
    private LocalDateTime startTime;

    public CreateScheduleResponse(Schedule schedule) {
        this.movieTitle = schedule.getMovie().getTitle();
        this.theaterName = schedule.getScreen().getTheater().getName();
        this.startTime = schedule.getStartTime();
    }
}
