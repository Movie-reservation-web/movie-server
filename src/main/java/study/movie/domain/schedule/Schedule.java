package study.movie.domain.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.movie.Movie;
import study.movie.domain.theater.Screen;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static study.movie.domain.schedule.ScheduleStatus.*;
import static study.movie.domain.schedule.SeatStatus.*;
import static study.movie.global.constants.StringAttrConst.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    private String scheduleNumber;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "screen_id")
    private Screen screen;

    @JsonIgnore
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.PERSIST)
    private final List<SeatEntity> seats = new ArrayList<>();

    private ScreenTime screenTime;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    private int totalSeatCount;
    private int reservedSeatCount;

    //==생성 메서드==//
    @Builder
    public Schedule(Movie movie, Screen screen, ScreenTime screenTime) {
        this.screenTime = screenTime;
        this.scheduleNumber = createScheduleNumber(screenTime.getStartDateTime());
        this.status = OPEN;
        this.reservedSeatCount = 0;
        addMovie(movie);
        addScreen(screen);
        initializeSeats(screen);
    }

    //==연관 관계 메서드==//
    /**
     * 영화 등록
     */
    public void addMovie(Movie movie) {
        this.movie = movie;
        movie.getSchedules().add(this);
    }

    /**
     * 상영관 등록
     */
    public void addScreen(Screen screen) {
        this.screen = screen;
        screen.getSchedules().add(this);
    }

    /**
     * 좌석 등록
     */
    private void addSeat(SeatEntity seat) {
        seats.add(seat);
        seat.addSchedule(this);
    }

    /**
     * 좌석 초기화
     */
    public void initializeSeats(Screen screen) {
        totalSeatCount = screen.getMaxCols() * screen.getMaxRows();
        for (int i = 1; i <= screen.getMaxRows(); i++) {
            for (int j = 1; j <= screen.getMaxCols(); j++) {
                SeatEntity seat = SeatEntity.builder()
                        .rowNum(i)
                        .colNum(j)
                        .build();
                addSeat(seat);
            }
        }
    }

    //==비즈니스 로직==//

    /**
     * 상영일정 번호 생성
     *
     * @return String
     */
    private String createScheduleNumber(LocalDateTime dateTime) {
        return String.valueOf(dateTime.getYear()).substring(2) +
                String.format("%02d", dateTime.getMonthValue()) +
                UUID.randomUUID().toString().substring(0, 9).toUpperCase(Locale.ROOT);
    }

    //==조회 로직==//

    /**
     * 전체 좌석 수
     *
     * @return String
     */
    public String getTotalSeatCountToString() {
        return TOTAL + getTotalSeatCount() + SEAT;
    }

    /**
     * 좌석 수 조회
     *
     * @return long
     */
    public int getSeatCount(SeatStatus status) {
        return (int) getSeats().stream()
                .filter(seatEntity -> seatEntity.getStatus() == status)
                .count();
    }

    /**
     * 예매된 좌석수 조회
     *
     * @return String
     */
    public String getReservedSeatValue() {
        if (getSeatCount(EMPTY) == 0) {
            status = ScheduleStatus.SOLD_OUT;
            return status.getValue();
        } else if (isClosed()) {
            status = ScheduleStatus.CLOSED;
            return status.getValue();
        }
        return status.convertSeatCount(getSeatCount(RESERVED));
    }

    /**
     * 예매 가능 여부
     *
     * @return boolean
     */
    private boolean isClosed() {
        return LocalDateTime.now().plusMinutes(40).isAfter(screenTime.getStartDateTime());
    }

    public void addReservedSeatCount(int size) {
        this.reservedSeatCount += size;
    }
}
