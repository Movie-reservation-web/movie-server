package study.movie.domain.schedule.dto.response;

import lombok.*;
import study.movie.domain.schedule.entity.Schedule;
import study.movie.domain.schedule.entity.ScreenTime;

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
