package study.movie.dto.schedule;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.movie.domain.schedule.Schedule;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ScheduleResponse {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalSeatCount;
    private Integer reservedSeatCount;

    public ScheduleResponse(Schedule schedule) {
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
        this.totalSeatCount = schedule.getTotalSeatCount();
        this.reservedSeatCount = schedule.getReservedSeat();
    }
}
