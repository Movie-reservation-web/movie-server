package study.movie.domain.theater;

import lombok.AccessLevel;
import lombok.Builder;
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
    @Builder
    public Seat(Character rowNum, Integer colNum, SeatStatus seatStatus) {
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.seatStatus = seatStatus;
    }

    //==비즈니스 로직==//
    public String seatToString() {
        return rowNum + String.valueOf(colNum);
    }
    public Integer getRowNum(){
        return (int) rowNum - 64;
    }
}
