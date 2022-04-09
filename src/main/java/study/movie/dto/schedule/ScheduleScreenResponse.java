package study.movie.dto.schedule;

import lombok.Data;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.ScreenTime;

@Data
public class ScheduleScreenResponse extends BaseScheduleResponse{

    private String screenFormat;
    private ScreenTime screenTime;
    private Integer totalSeatCount;
    private Integer reservedSeatCount;

    public ScheduleScreenResponse(Schedule schedule) {
        this.id = schedule.getId();
        this.screenFormat = schedule.getScreen().getFormat().getValue();
        this.screenTime = schedule.getScreenTime();
        this.totalSeatCount = schedule.getTotalSeatCount();
        this.reservedSeatCount = schedule.getReservedSeatCount();
    }
}
