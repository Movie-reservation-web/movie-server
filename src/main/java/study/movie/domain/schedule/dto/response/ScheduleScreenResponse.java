package study.movie.domain.schedule.dto.response;

import lombok.*;
import study.movie.domain.schedule.entity.Schedule;
import study.movie.domain.schedule.entity.ScreenTime;
import study.movie.domain.theater.entity.ScreenFormat;

@Data
@Builder
public class ScheduleScreenResponse{
    private String scheduleNumber;
    private ScreenFormat screenFormat;
    private String screenName;
    private ScreenTime screenTime;
    private String totalSeatCount;
    private String reservedSeatCount;

    public static ScheduleScreenResponse of(Schedule schedule) {
        return ScheduleScreenResponse.builder()
                .scheduleNumber(schedule.getScheduleNumber())
                .screenName(schedule.getScreen().getName())
                .screenFormat(schedule.getScreen().getFormat())
                .screenTime(schedule.getScreenTime())
                .totalSeatCount(schedule.getTotalSeatCountToString())
                .reservedSeatCount(schedule.getReservedSeatValue())
                .build();
    }
}
