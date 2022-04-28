package study.movie.dto.schedule.response;

import lombok.*;
import study.movie.domain.schedule.SeatEntity;
import study.movie.domain.schedule.SeatStatus;

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
