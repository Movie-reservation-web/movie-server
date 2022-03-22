package study.movie.domain.theater;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.schedule.Seat;
import study.movie.domain.schedule.SeatStatus;
import study.movie.repository.theater.ScreenRepository;
import study.movie.repository.theater.TheaterRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static study.movie.domain.schedule.SeatStatus.EMPTY;
import static study.movie.domain.schedule.SeatStatus.RESERVING;
import static study.movie.domain.theater.ScreenFormat.*;
import static study.movie.domain.theater.ScreenFormat.SCREEN_X;


@SpringBootTest
@Transactional
public class ScreenTest {

    @Autowired
    ScreenRepository screenRepository;

    @Autowired
    TheaterRepository theaterRepository;

    Theater theater;
    List<ScreenFormat> screenFormats;
    List<Seat> seats;

    @BeforeEach
    public void setUp() {
        theater = Theater.builder()
                .city("용산")
                .name("아이파크몰")
                .phone("1111-1111")
                .build();

        theaterRepository.save(theater);

        screenFormats = Arrays.asList(FOUR_D_FLEX_SCREEN, SCREEN_X, GOLD_CLASS);
        seats = new ArrayList<>();
        IntStream.range(0,10).forEach(i -> seats.add(Seat.createSeat('A', i)));
    }

    @Test
    public void 극장_상영관_등록() throws Exception {
        // given
        // when
        Screen savedScreen = Screen.createScreen("1관", screenFormats, seats, theater);

        // then
        Assertions.assertEquals(savedScreen.getTheater(), theater);
    }

    @Test
    public void 상영관_좌석_개수_조회() throws Exception {
        // given
        Screen savedScreen = Screen.createScreen("1관", screenFormats, seats, theater);

        // when
        int seatsCount = savedScreen.getSeatsCount(EMPTY);

        // then
        Assertions.assertEquals(seatsCount, seats.size());
    }

    @Test
    public void 상영관_EMPTY_좌석_조회() throws Exception {
        // given
        Screen savedScreen = Screen.createScreen("1관", screenFormats, seats, theater);

        // when
        List<Seat> findSeats = savedScreen.getSeats(EMPTY);

        // then
        Assertions.assertEquals(findSeats, seats);
    }
    
    @Test
    public void 상영관_좌석_상태_변경() throws Exception {
        // given
        Seat testSeat = seats.get(0);
        Screen savedScreen = Screen.createScreen("1관", screenFormats, seats, theater);

        // when
        savedScreen.updateSeatStatus(testSeat, RESERVING);

        // then
        Assertions.assertTrue(savedScreen.getSeats(RESERVING).contains(testSeat));

    }
    @Test
    public void 상영관_좌석_상태_변경_없는_좌석_Exception() throws Exception {
        // given
        Screen savedScreen = Screen.createScreen("1관", screenFormats, seats, theater);

        // when
        Seat noneSeat = Seat.createSeat('B', 2);

        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> savedScreen.updateSeatStatus(noneSeat, RESERVING));

    }
    @Test
    public void 상영관_예매_가능_좌석_확인() throws Exception {
        // given
        Screen savedScreen = Screen.createScreen("1관", screenFormats, seats, theater);

        // when
        Seat possibleSeat = seats.get(0);
        Seat impossibleSeat = Seat.createSeat('B', 1);

        // then
        Assertions.assertTrue(savedScreen.isAvailableSeat(possibleSeat));
        Assertions.assertFalse(savedScreen.isAvailableSeat(impossibleSeat));
    }


}
