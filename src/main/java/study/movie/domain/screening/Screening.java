package study.movie.domain.screening;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.movie.Movie;
import study.movie.domain.screen.Screen;
import study.movie.domain.seat.Seat;
import study.movie.domain.ticket.Ticket;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "screening")
    private List<Seat> seats = new ArrayList<>();

    @OneToMany(mappedBy = "screening")
    private List<Ticket> tickets = new ArrayList<>();

    @Builder
    public Screening(LocalDateTime startTime, Movie movie, Screen screen) {
        this.startTime = startTime;
        this.setMovie(movie);
        this.setScreen(screen);
    }

    public void setMovie(Movie movie) {
        if (movie == null) {
            return;
        }
        this.movie = movie;
        movie.getScreenings().add(this);
    }

    public void setScreen(Screen screen) {
        if (screen == null) {
            return;
        }
        this.screen = screen;
        screen.getScreenings().add(this);
    }

    public void addSeat(Seat seat) {
        seats.add(seat);
        reservedSeat++;
    }

    public void removeSeat(Seat seat) {
        seats.remove(seat);
        reservedSeat--;
    }
}
