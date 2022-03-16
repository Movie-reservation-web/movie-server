package study.movie.domain.schedule;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import static study.movie.global.constants.EntityAttrConst.SeatStatus;

@Embeddable
@Data
public class Seat {

    private Character rowNum;
    private Integer colNum;

    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus;

    public String seatToString() {
        return rowNum + String.valueOf(colNum);
    }
}
