package study.movie.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import study.movie.domain.schedule.Schedule;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ScheduleScreenResponse extends BaseScheduleResponse{

    private String screenFormat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-EEE", timezone = "Asia/Seoul")
    private LocalDateTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime endTime;
    private Integer totalSeatCount;
    private Integer reservedSeatCount;

    public ScheduleScreenResponse(Schedule schedule) {
        this.id = schedule.getId();
        this.screenFormat = schedule.getScreen().getFormat().getValue();
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
        this.totalSeatCount = schedule.getTotalSeatCount();
        this.reservedSeatCount = schedule.getReservedSeatCount();
    }
}
