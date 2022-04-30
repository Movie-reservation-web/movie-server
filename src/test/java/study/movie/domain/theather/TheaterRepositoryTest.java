package study.movie.domain.theather;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TheaterRepositoryTest {

    @Autowired
    TheaterRepository theaterRepository;

    @Test
    void 극장을_조회한다() {
        // given
        Theater theater = Theater.builder()
                .city("서울")
                .name("영등포 CGV")
                .phone("02-4940-1002")
                .build();

        Theater savedTheater = theaterRepository.save(theater);

        // when
        Theater findTheater = theaterRepository.findById(savedTheater.getId()).get();

        // then
        Assertions.assertThat(findTheater).isEqualTo(savedTheater);
    }
}