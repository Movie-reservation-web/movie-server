package study.movie.domain.schedule;

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

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.PERSIST)
    private final List<SeatEntity> seats = new ArrayList<>();

    private ScreenTime screenTime;

    private Integer reservedSeatCount;

    //==생성 메서드==//
    @Builder
    public Schedule(Movie movie, Screen screen, ScreenTime screenTime) {
        this.screenTime = screenTime;
        this.reservedSeatCount = 0;
        this.scheduleNumber = createScheduleNumber(screenTime.getStartDateTime());
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
     */
    private String createScheduleNumber(LocalDateTime dateTime) {
        return String.valueOf(dateTime.getYear()).substring(2) +
                String.format("%02d", dateTime.getMonthValue()) +
                UUID.randomUUID().toString().substring(0, 9).toUpperCase(Locale.ROOT);
    }

    //==조회 로직==//
    /**
     * 전체 좌석 수
     */
    public int getTotalSeatCount() {
        return getSeats().size();
    }

    /**
     * 좌석 수 조회
     */
    public int getReservedSeatCount(SeatStatus status) {
        return (int) getSeats().stream().filter(seatEntity -> seatEntity.getStatus() == status).count();
    }

}
