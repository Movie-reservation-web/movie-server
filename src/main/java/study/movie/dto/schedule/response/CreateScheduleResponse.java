package study.movie.dto.schedule.response;

import lombok.*;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.ScreenTime;

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
