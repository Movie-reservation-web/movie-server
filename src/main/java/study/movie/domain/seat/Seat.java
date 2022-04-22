package study.movie.domain.seat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.screening.Screening;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Seat {

    @Id
    @GeneratedValue
    @Column(name = "seat_id")
    private Long id;

    @Column(nullable = false)
    private Long seatRow;

    @Column(nullable = false)
    private Long seatColumn;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screening_id")
    private Screening screening;

    @Builder
    public Seat(Long seatRow, Long seatColumn, Screening screening, SeatStatus seatStatus) {
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.seatStatus = seatStatus;
        setScreening(screening);
    }

    void setScreening(Screening screening) {
        if (screening == null) {
            return;
        }
        this.screening = screening;
        screening.addSeat(this);
    }
}