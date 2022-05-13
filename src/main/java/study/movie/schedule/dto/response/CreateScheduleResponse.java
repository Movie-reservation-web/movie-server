package study.movie.schedule.dto.response;

import lombok.*;
import study.movie.schedule.entity.Schedule;
import study.movie.schedule.entity.ScreenTime;

@Data
@Builder
public class CreateScheduleResponse {

    private String movieTitle;
    private String theaterName;

    private ScreenTime screenTime;

    public static CreateScheduleResponse of(Schedule schedule) {
        return CreateScheduleResponse.builder()
                .movieTitle(schedule.getMovie().getTitle())
                .theaterName(schedule.getScreen().getTheater().getName())
                .screenTime(schedule.getScreenTime())
                .build();
    }
}
