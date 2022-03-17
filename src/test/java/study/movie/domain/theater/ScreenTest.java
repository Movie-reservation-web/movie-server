package study.movie.domain.theater;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.schedule.Seat;
import study.movie.domain.theater.repository.ScreenRepository;
import study.movie.domain.theater.repository.TheaterRepository;
import study.movie.global.constants.EntityAttrConst;
import study.movie.global.constants.EntityAttrConst.ScreenFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static study.movie.global.constants.EntityAttrConst.ScreenFormat.*;
import static study.movie.global.constants.EntityAttrConst.SeatStatus.EMPTY;

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
        for (int i = 0; i < 10; i++) {
            seats.add(Seat.createSeat('A', i, EMPTY));
        }

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

}
