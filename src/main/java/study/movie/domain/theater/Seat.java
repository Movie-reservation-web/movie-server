package study.movie.domain.theater;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import static study.movie.global.constants.EntityAttrConst.SeatStatus;

@Embeddable
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    private Character rowNum;
    private Integer colNum;

    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus;


    //==생성 메서드==//
    public static Seat createSeat(Character rowNum, Integer colNum) {
        Seat seat = new Seat();
        seat.setRowNum(rowNum);
        seat.setColNum(colNum);
        seat.setSeatStatus(SeatStatus.EMPTY);
        return seat;
    }

    //==비즈니스 로직==//
    public String seatToString() {
        return rowNum + String.valueOf(colNum);
    }
    public Integer getRowNum(){
        return (int) rowNum - 64;
    }
}
