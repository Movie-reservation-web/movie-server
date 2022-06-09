package study.movie.theater.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
@Slf4j
class ScreenTest {

    @Autowired
    EntityManager em;

    @Autowired
    InitService init;

    @Test
    public void 상영관_생성() {

    }

    @Test
    public void 상영관_수정() {

    }

    @Test
    public void 상영관_검증() {

    }
}
