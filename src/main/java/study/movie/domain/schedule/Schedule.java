package study.movie.domain.schedule;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.movie.Movie;
import study.movie.domain.theater.Screen;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "screen_id")
    private Screen screen;

    @ElementCollection
    @CollectionTable(name = "seat", joinColumns = @JoinColumn(name = "schedule_id"))
    @MapKeyColumn
    @Column(name = "reserve_status")
    @Enumerated(EnumType.STRING)
    private Map<Seat, ReservationStatus> seats = new ConcurrentHashMap<>();


    private LocalDateTime startTime;
    private Integer reservedSeatCount;

    //==생성 메서드==//
    @Builder
    public Schedule(Movie movie, Screen screen, LocalDateTime startTime) {
        this.startTime = startTime;
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
     * 좌석 초기화
     */
    public void initializeSeats(Screen screen) {
        for (int i = 1; i <= screen.getMaxRows(); i++) {
            for (int j = 1; j <= screen.getMaxCols(); j++) {
                getSeats().put(
                        Seat.builder()
                                .rowNum(i)
                                .colNum(j)
                                .build()
                        ,ReservationStatus.EMPTY);
            }
        }
    }

    //==조회 로직==//
    /**
     * 종료 시간 조회
     */
    public LocalTime getEndTime() {
        return startTime.toLocalTime().plus(Duration.ofMinutes(movie.getRunningTime()));
    }

    /**
     * 전체 좌석 수
     */
    public int getTotalSeatCount() {
        return getSeats().size();
    }

    /**
     * 좌석 수 조회
     * @param status
     */
    public int getReservedSeatCount(ReservationStatus status){
        return (int) getSeats().values().stream().filter(value -> value == status).count();
    }

}
