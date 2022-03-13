package study.movie.app.screen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.app.screen.converter.ScreenFormatConverter;
import study.movie.app.theather.entity.Theater;
import study.movie.global.constants.EntityAttrConst.SeatStatus;
import study.movie.global.constants.EntityAttrConst.ScreenFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "screens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screen_id")
    private Long id;

    private String name;

    @Convert(converter = ScreenFormatConverter.class)
    private List<ScreenFormat> formats;

    @ElementCollection
    @CollectionTable(name = "seat", joinColumns = @JoinColumn(name = "screen_id"))
    private List<Seat> seats;

    private Integer capacity;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @JsonIgnore
    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>();

    @Builder
    public Screen(String name, List<ScreenFormat> formats, List<Seat> seats, Integer capacity) {
        this.name = name;
        this.formats = formats;
        this.seats = seats;
        this.capacity = capacity;
    }

    //==생성 메서드==//
    public static Screen createScreen(String name, List<ScreenFormat> formats, List<Seat> seats, Theater theater) {
        Screen screen = Screen.builder()
                .name(name)
                .formats(formats)
                .seats(seats)
                .capacity(seats.size())
                .build();
        screen.registerTheater(theater);
        return screen;
    }

    //==연관 관계 메서드==//
    public void registerTheater(Theater theater) {
        this.theater = theater;
        theater.getScreens().add(this);
    }

    //==조회 로직==//
    public int getSeatsCount(SeatStatus status) {
        return Optional.of(
                seats.stream()
                        .filter(seat -> seat.getStatus() == status)
                        .count())
                .orElse(0L)
                .intValue();
    }

    public List<Seat> getSeats(SeatStatus status) {
        return Optional.of(
                seats.stream()
                        .filter(seat -> seat.getStatus() == status)
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }
}
