package study.movie.domain.schedule;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    private Integer rowNum;
    private Integer colNum;

    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    //==생성 메서드==//
    @Builder
    public Seat(Integer rowNum, Integer colNum, SeatStatus seatStatus) {
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.seatStatus = seatStatus;
    }

    //==비즈니스 로직==//
    public String seatToString() {
        return getRowNum() + String.valueOf(colNum);
    }
    public char getRowNum(){
        return (char) (rowNum + 64);
    }

    public void registerSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
