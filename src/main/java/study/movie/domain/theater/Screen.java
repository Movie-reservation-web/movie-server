package study.movie.domain.theater;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.converter.theater.ScreenFormatConverter;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.Seat;
import study.movie.global.constants.EntityAttrConst.ScreenFormat;
import study.movie.global.constants.EntityAttrConst.SeatStatus;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Screen extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screen_id")
    private Long id;

    private String name;

    @Convert(converter = ScreenFormatConverter.class)
    private List<ScreenFormat> formats;

    @ElementCollection
    @CollectionTable(name = "seat",
            joinColumns = @JoinColumn(name = "screen_id")
    )
    private List<Seat> seats = new ArrayList<>();

    private Integer capacity;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @JsonIgnore
    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>();

    @Builder
    public Screen(String name, List<ScreenFormat> formats, List<Seat> seats) {
        this.name = name;
        this.formats = formats;
        this.seats = seats;
        this.capacity = seats.size();
    }

    //==생성 메서드==//
    public static Screen createScreen(String name, List<ScreenFormat> formats, List<Seat> seats, Theater theater) {
        Screen screen = Screen.builder()
                .name(name)
                .formats(formats)
                .seats(seats)
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
                getSeats().stream()
                        .filter(seat -> seat.getSeatStatus() == status)
                        .count())
                .orElse(0L)
                .intValue();
    }

    public List<Seat> getSeats(SeatStatus status) {
        return Optional.of(
                getSeats().stream()
                        .filter(seat -> seat.getSeatStatus() == status)
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }

    //==비즈니스 로직==//

    /**
     * 예매 가능한 좌석인지
     */
    public boolean isAvailableSeat(Seat seat) {
        return getSeats().stream()
                .anyMatch(s -> isEmptySeat(s, seat));
    }

    /**
     * 좌석 상태 변경
     */
    public void updateSeatStatus(Seat seat, SeatStatus status) {
        getSeats().stream()
                .filter(s -> isEmptySeat(s, seat))
                .findAny()
                .orElseThrow(IllegalArgumentException::new)
                .setSeatStatus(status);
    }

    private boolean isEmptySeat(Seat seatA, Seat seatB) {
        return seatA.seatToString().equals(seatB.seatToString())
                && seatA.getSeatStatus() == SeatStatus.EMPTY;
    }
}
