package study.movie.dto.schedule.response;

import lombok.Data;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.ScreenTime;

@Data
public class CreateScheduleResponse {

    private String movieTitle;
    private String theaterName;
    private ScreenTime screenTime;

    public CreateScheduleResponse(Schedule schedule) {
        this.movieTitle = schedule.getMovie().getTitle();
        this.theaterName = schedule.getScreen().getTheater().getName();
        this.screenTime = schedule.getScreenTime();
    }
}
