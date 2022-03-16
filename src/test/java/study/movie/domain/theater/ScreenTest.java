package study.movie.domain.theater;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static study.movie.global.constants.EntityAttrConst.ScreenFormat.*;
import static study.movie.global.constants.EntityAttrConst.SeatStatus.EMPTY;

@SpringBootTest
@Transactional
@Commit
public class ScreenTest {

    @Autowired
    ScreenRepository screenRepository;

    @Autowired
    TheaterRepository theaterRepository;

    @BeforeEach
    public void setUp(){
        Theater theater = Theater.builder()
                .city("용산")
                .name("아이파크몰")
                .phone("1111-1111")
                .build();
        theaterRepository.save(theater);

        List<EntityAttrConst.ScreenFormat> screenFormats = Arrays.asList(FOUR_D_FLEX_SCREEN, SCREEN_X, GOLD_CLASS);
        List<Seat> seats = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            seats.add(Seat.createSeat('A', i, EMPTY));
        }

        Screen.createScreen("1관", screenFormats, seats, theater);
    }

    @Test
    public void 극장_상영관_등록() throws Exception {
        // given
        System.out.println("screenRepository = " + screenRepository.count());
        System.out.println("screenRepository = " + theaterRepository.count());
        // when

        // then
    }

}
