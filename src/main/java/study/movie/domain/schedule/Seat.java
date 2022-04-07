package study.movie.domain.schedule;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    private Integer rowNum;
    private Integer colNum;

    @Builder
    public Seat(Integer rowNum, Integer colNum) {
        this.rowNum = rowNum;
        this.colNum = colNum;
    }

    public String seatToString() {
        return (char) (rowNum + '@') + String.valueOf(colNum);
    }
}
