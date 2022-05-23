package study.movie.domain.schedule.dto.response;

import lombok.*;
import study.movie.domain.schedule.entity.Schedule;
import study.movie.domain.schedule.entity.ScreenTime;

@Data
@Builder
public class ScheduleScreenResponse{
    private Long id;
    private String screenFormat;
    private String screenName;
    private ScreenTime screenTime;
    private String totalSeatCount;
    private String reservedSeatCount;

    public static ScheduleScreenResponse of(Schedule schedule) {
        return ScheduleScreenResponse.builder()
                .id(schedule.getId())
                .screenName(schedule.getScreen().getName())
                .screenFormat(schedule.getScreen().getFormat().getValue())
                .screenTime(schedule.getScreenTime())
                .totalSeatCount(schedule.getTotalSeatCountToString())
                .reservedSeatCount(schedule.getReservedSeatValue())
                .build();
    }
}
