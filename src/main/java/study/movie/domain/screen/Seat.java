package study.movie.domain.screen;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import static study.movie.global.constants.EntityAttrConst.SeatStatus;

@Embeddable
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    private Integer row;
    private Integer column;

    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus;

    @Builder
    public Seat(Integer row, Integer column, SeatStatus seatStatus) {
        this.row = row;
        this.column = column;
        this.seatStatus = seatStatus;
    }
}
