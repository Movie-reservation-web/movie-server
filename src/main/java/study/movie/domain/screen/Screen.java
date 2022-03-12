package study.movie.domain.screen;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.screening.Screening;
import study.movie.domain.theather.Theater;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Screen {

    @Id
    @GeneratedValue
    @Column(name = "screen_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private Long screenRow;

    @Column(nullable = false)
    private Long screenColumn;

    @Column(nullable = false)
    private Long capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ScreenType type;

    @OneToMany(mappedBy = "screen")
    private List<Screening> screenings = new ArrayList<>();

    @Builder
    public Screen(String name, Long screenRow, Long screenColumn,
                  Long capacity, Theater theater, ScreenType type) {
        this.name = name;
        this.screenRow = screenRow;
        this.screenColumn = screenColumn;
        this.capacity = capacity;
        this.type = type;
        setTheater(theater);
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
        theater.getScreenList().add(this);
    }
}
