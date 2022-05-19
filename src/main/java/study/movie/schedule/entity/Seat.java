package study.movie.schedule.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"rowNum", "colNum"})
public class Seat {

    private Integer rowNum;
    private Integer colNum;

    public Seat(Integer rowNum, Integer colNum) {
        this.rowNum = rowNum;
        this.colNum = colNum;
    }

    public String seatToString() {
        return (char) (rowNum + '@') + String.valueOf(colNum);
    }

    public static Seat stringToSeat(String data) {
        return new Seat(data.charAt(0) - '@', Integer.parseInt(data.substring(1)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowNum, colNum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return Objects.equals(rowNum, seat.rowNum) &&
                Objects.equals(colNum, seat.colNum);
    }
}
