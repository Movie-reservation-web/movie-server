package study.movie.dto.schedule.response;

import lombok.Data;
import study.movie.domain.schedule.SeatEntity;
import study.movie.domain.schedule.SeatStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class SeatResponse{

    private char row;
    private int column;
    private SeatStatus status;

    public SeatResponse(SeatEntity seatEntity) {
        this.row = (char) (seatEntity.getSeat().getRowNum() + '@');
        this.column = seatEntity.getSeat().getColNum();
        this.status = seatEntity.getStatus();
    }
}
