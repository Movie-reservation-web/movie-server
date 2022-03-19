package study.movie.domain.theater.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.schedule.Seat;
import study.movie.domain.theater.Screen;
import study.movie.global.constants.EntityAttrConst;
import study.movie.global.constants.EntityAttrConst.ScreenFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static study.movie.global.constants.EntityAttrConst.ScreenFormat.*;
import static study.movie.global.constants.EntityAttrConst.SeatStatus.*;

@SpringBootTest
@Transactional
class ScreenRepositoryTest {
    @Autowired
    ScreenRepository screenRepository;

    @Test
    public void 상영관_저장_조회() throws Exception {
        // given
        List<ScreenFormat> screenFormats = Arrays.asList(FOUR_D_FLEX_SCREEN, SCREEN_X, GOLD_CLASS);
        List<Seat> seats = new ArrayList<>();
        IntStream.range(0,10).forEach(i -> seats.add(Seat.createSeat('A', i)));

        Screen screen = Screen.builder()
                .name("1관")
                .formats(screenFormats)
                .seats(seats)
                .build();

        Screen savedScreen = screenRepository.save(screen);

        // when
        Screen findScreen = screenRepository.findById(savedScreen.getId()).get();

        // then
        Assertions.assertEquals(savedScreen, findScreen);
    }

}