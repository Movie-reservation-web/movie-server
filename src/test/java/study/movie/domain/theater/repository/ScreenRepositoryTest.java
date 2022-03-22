package study.movie.domain.theater.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.theater.Screen;
import study.movie.global.constants.EntityAttrConst.ScreenFormat;
import study.movie.repository.theater.ScreenRepository;

import java.util.Arrays;
import java.util.List;

import static study.movie.global.constants.EntityAttrConst.ScreenFormat.*;

@SpringBootTest
@Transactional
class ScreenRepositoryTest {
    @Autowired
    ScreenRepository screenRepository;

    @Test
    public void 상영관_저장_조회() throws Exception {
        // given
        List<ScreenFormat> screenFormats = Arrays.asList(FOUR_D_FLEX_SCREEN, SCREEN_X, GOLD_CLASS);

        Screen screen = Screen.builder()
                .name("1관")
                .maxCols(5)
                .maxRows(10)
                .formats(screenFormats)
                .build();

        Screen savedScreen = screenRepository.save(screen);

        // when
        Screen findScreen = screenRepository.findById(savedScreen.getId()).get();

        // then
        Assertions.assertEquals(savedScreen, findScreen);
    }

}