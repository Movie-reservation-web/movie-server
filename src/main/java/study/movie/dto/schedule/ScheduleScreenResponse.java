package study.movie.dto.schedule;

import lombok.Data;
import study.movie.domain.schedule.Schedule;

import java.time.LocalDateTime;

@Data
public class ScheduleScreenResponse extends BaseScheduleResponse{

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalSeatCount;
    private Integer reservedSeatCount;

    public ScheduleScreenResponse(Schedule schedule) {
        this.id = schedule.getId();
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
        this.totalSeatCount = schedule.getTotalSeatCount();
        this.reservedSeatCount = schedule.getReservedSeatCount();
    }
}
