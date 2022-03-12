package study.movie.domain.screening;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.movie.Movie;
import study.movie.domain.screen.Screen;

import javax.persistence.*;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Screening {

    @Id
    @GeneratedValue
    @Column(name = "screening_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private Long reservedSeat = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id")
    private Screen screen;

    @Builder
    public Screening(LocalDateTime startTime, Movie movie, Screen screen) {
        this.startTime = startTime;
        setMovie(movie);
        setScreen(screen);
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        movie.getScreenings().add(this);
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
        screen.getScreenings().add(this);
    }
}
