package study.movie.domain.schedule;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "seat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    private Seat seat;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Builder
    public SeatEntity(int rowNum, int colNum) {
        this.seat = new Seat(rowNum, colNum);
        this.status = SeatStatus.EMPTY;
    }


    //==비즈니스 로직==//
    public void updateStatus(SeatStatus status) {
        this.status = status;
    }

    public void addSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
