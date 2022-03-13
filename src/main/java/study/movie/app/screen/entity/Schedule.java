package study.movie.app.screen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.app.intro.entity.Movie;
import study.movie.app.reserve.entity.Ticket;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "schedules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {
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

    private LocalDateTime startTime;

    //==생성 메서드==//
    @Builder
    public Schedule(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public static Schedule createSchedule(Movie movie, Screen screen, LocalDateTime startTime) {
        Schedule schedule = Schedule.builder().startTime(startTime).build();
        schedule.addMovie(movie);
        schedule.addScreen(screen);
        return schedule;
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

    //==조회 로직==//
    /**
     * 종료 시간 조회
     */
    public LocalDateTime getEndTime() {
        return startTime.plus(Duration.ofMinutes(movie.getRunningTime()));
    }
}
