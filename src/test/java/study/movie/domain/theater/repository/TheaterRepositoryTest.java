package study.movie.domain.theater.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.theater.Theater;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TheaterRepositoryTest {
    @Autowired
    TheaterRepository theaterRepository;

    @Test
    public void 극장_저장_조회() throws Exception {
        // given
        Theater theater = Theater.builder()
                .city("용산")
                .name("아이파크몰")
                .phone("1111-1111")
                .build();

        Theater savedTheater = theaterRepository.save(theater);

        // when
        Theater findTheater = theaterRepository.findById(savedTheater.getId()).get();

        // then
        Assertions.assertEquals(savedTheater, findTheater);
    }

}