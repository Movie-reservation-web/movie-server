package study.movie.domain.screen;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ScreenRepositoryTest {
//
//    @Autowired
//    TheaterRepository theaterRepository;
//
//    @Autowired
//    ScreenRepository screenRepository;
//
//    @Test
//    void 상영관을_조회한다() {
//        // given
//        Theater theater = Theater.builder()
//                .city("서울")
//                .name("영등포 CGV")
//                .phone("02-4940-1002")
//                .build();
//        Theater savedTheater = theaterRepository.save(theater);
//
//        Screen screen = Screen.builder()
//                .name("6관")
//                .capacity(320L)
//                .screenRow(10L)
//                .screenColumn(32L)
//                .type(ScreenType.IMAX)
//                .theater(savedTheater)
//                .build();
//        Screen savedScreen = screenRepository.save(screen);
//
//        // when
//        Screen findScreen = screenRepository.findById(savedScreen.getId()).get();
//
//        // then
//        Assertions.assertThat(findScreen).isEqualTo(savedScreen);
//    }
}