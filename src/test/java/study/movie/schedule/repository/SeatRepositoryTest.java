package study.movie.schedule.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
@Slf4j
@Commit
class SeatRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    ScheduleRepository scheduleRepository;

//    @Test
//    public void 전체_좌석_조회() throws Exception {
//        // given
//        Theater theater = createTheater("용산 CGV", CityCode.SEL, "000-000");
//        Screen screen = registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
//        Movie movie = createMovie("영화1", "홍길동");
//
//        ScreenTime screenTime = new ScreenTime(LocalDateTime.of(2022, 3, 10, 3, 2, 21), movie.getRunningTime());
//        Schedule savedSchedule = Schedule.builder()
//                .screenTime(screenTime)
//                .screen(screen)
//                .movie(movie)
//                .build();
//
//        em.flush();
//        Long savedId = savedSchedule.getId();
//
//        // when
//        int size = savedSchedule.getSeats().size();
//        List<SeatEntity> seatByQuery = scheduleRepository.findScheduleWithSeat(savedId);
//
//        // then
//        assertEquals(size, seatByQuery.size());
//
//    }


}