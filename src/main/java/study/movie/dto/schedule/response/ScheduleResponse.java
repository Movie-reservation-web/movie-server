package study.movie.dto.schedule.response;

import lombok.Builder;
import lombok.Data;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.ScheduleStatus;
import study.movie.domain.schedule.ScreenTime;

@Data
@Builder
public class ScheduleResponse {
    private Long id;
    private String scheduleNumber;
    private MovieResponse movie;
    private ScreenResponse screen;
//    private int totalSeatCount;
//    private int reservedSeatCount;
    private ScreenTime screenTime;
    private ScheduleStatus status;

    public static ScheduleResponse of(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .scheduleNumber(schedule.getScheduleNumber())
                .movie(MovieResponse.of(schedule.getMovie()))
                .screen(ScreenResponse.of(schedule.getScreen()))
//                .totalSeatCount(schedule.getTotalSeatCount())
//                .reservedSeatCount(schedule.getSeatCount(RESERVED))
                .screenTime(schedule.getScreenTime())
                .status(schedule.getStatus())
                .build();
    }
}
