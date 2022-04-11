package study.movie.dto.schedule;

import lombok.Data;
import study.movie.domain.schedule.SeatStatus;
import study.movie.domain.schedule.Seat;

@Data
public class ScheduleSeatResponse {

    private int rowNum;
    private int colNum;
    private SeatStatus status;

    public ScheduleSeatResponse(Seat seat, SeatStatus status) {
        this.rowNum = seat.getRowNum();
        this.colNum = seat.getColNum();
        this.status = status;
    }
}
