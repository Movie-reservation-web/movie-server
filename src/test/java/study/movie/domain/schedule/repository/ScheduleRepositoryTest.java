package study.movie.domain.schedule.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.schedule.Schedule;
import study.movie.repository.schedule.ScheduleRepository;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
class ScheduleRepositoryTest {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Test
    public void 상영_일정_저장_조회() throws Exception {
        // given
        Schedule schedule = Schedule.builder().startTime(LocalDateTime.now()).build();
        Schedule savedSchedule = scheduleRepository.save(schedule);

        // when
        Schedule findSchedule = scheduleRepository.findById(savedSchedule.getId()).get();

        // then
        Assertions.assertEquals(savedSchedule, findSchedule);
    }

}