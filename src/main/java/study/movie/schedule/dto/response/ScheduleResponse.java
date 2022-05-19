package study.movie.schedule.dto.response;

import lombok.Builder;
import lombok.Data;
import study.movie.schedule.entity.Schedule;
import study.movie.schedule.entity.ScheduleStatus;
import study.movie.schedule.entity.ScreenTime;
import study.movie.schedule.entity.SeatStatus;

@Data
@Builder
public class ScheduleResponse {
    private Long id;
    private String scheduleNumber;
    private MovieResponse movie;
    private ScreenResponse screen;
    private int totalSeatCount;
    private int reservedSeatCount;
    private ScreenTime screenTime;
    private ScheduleStatus status;


    public static ScheduleResponse of(Schedule schedule,Long count) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .scheduleNumber(schedule.getScheduleNumber())
                .movie(MovieResponse.of(schedule.getMovie()))
                .screen(ScreenResponse.of(schedule.getScreen()))
                .totalSeatCount(schedule.getTotalSeatCount())
                .reservedSeatCount(Math.toIntExact(count))
                .screenTime(schedule.getScreenTime())
                .status(schedule.getStatus())
                .build();
    }
    public static ScheduleResponse of(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .scheduleNumber(schedule.getScheduleNumber())
                .movie(MovieResponse.of(schedule.getMovie()))
                .screen(ScreenResponse.of(schedule.getScreen()))
                .totalSeatCount(schedule.getTotalSeatCount())
                .reservedSeatCount(schedule.getSeatCount(SeatStatus.RESERVED))
                .screenTime(schedule.getScreenTime())
                .status(schedule.getStatus())
                .build();
    }
}
