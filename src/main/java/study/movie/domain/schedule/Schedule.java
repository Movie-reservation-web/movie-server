package study.movie.domain.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.movie.Movie;
import study.movie.domain.theater.Screen;
import study.movie.domain.ticket.Ticket;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "schedule")
    private List<Seat> seats = new ArrayList<>();

    private LocalDateTime startTime;
    private Integer reservedSeat;

    //==생성 메서드==//
    @Builder
    public Schedule(Movie movie, Screen screen, LocalDateTime startTime, Integer reservedSeat) {
        this.startTime = startTime;
        this.reservedSeat = 0;
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
                Seat seat = Seat.builder()
                        .rowNum(i)
                        .colNum(j)
                        .seatStatus(SeatStatus.EMPTY)
                        .build();
                getSeats().add(seat);
            }
        }
    }

    //==조회 로직==//
    /**
     * 종료 시간 조회
     */
    public LocalDateTime getEndTime() {
        return startTime.plus(Duration.ofMinutes(movie.getRunningTime()));
    }

    /**
     * 전체 좌석 수
     */
    public int getTotalSeatCount(){
        return getSeats().size();
    }
}
