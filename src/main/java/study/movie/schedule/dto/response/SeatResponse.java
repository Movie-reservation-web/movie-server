package study.movie.schedule.dto.response;

import lombok.*;
import study.movie.schedule.entity.SeatEntity;
import study.movie.schedule.entity.SeatStatus;

@Data
@Builder
public class SeatResponse {

    private char row;
    private int column;
    private SeatStatus status;

    public static SeatResponse of(SeatEntity seatEntity) {
        return SeatResponse.builder()
                .row((char) (seatEntity.getSeat().getRowNum() + '@'))
                .column(seatEntity.getSeat().getColNum())
                .status(seatEntity.getStatus())
                .build();
    }
}
