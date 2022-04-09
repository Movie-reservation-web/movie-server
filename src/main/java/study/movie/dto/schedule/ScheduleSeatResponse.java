package study.movie.dto.schedule;

import lombok.Data;
import study.movie.domain.schedule.ReservationStatus;
import study.movie.domain.schedule.Seat;

@Data
public class ScheduleSeatResponse {

    private int rowNum;
    private int colNum;
    private ReservationStatus status;

    public ScheduleSeatResponse(Seat seat, ReservationStatus status) {
        this.rowNum = seat.getRowNum();
        this.colNum = seat.getColNum();
        this.status = status;
    }
}
