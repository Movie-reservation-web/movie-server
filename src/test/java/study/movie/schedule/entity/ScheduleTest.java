package study.movie.schedule.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.movie.entity.Movie;
import study.movie.schedule.repository.ScheduleRepository;
import study.movie.theater.entity.CityCode;
import study.movie.theater.entity.Screen;
import study.movie.theater.entity.ScreenFormat;
import study.movie.theater.entity.Theater;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static study.movie.global.constants.StringAttrConst.SEAT;
import static study.movie.global.constants.StringAttrConst.TOTAL;
import static study.movie.schedule.entity.SeatStatus.RESERVED;

@SpringBootTest
@Transactional
@Slf4j
@Rollback
class ScheduleTest {

    @Autowired
    EntityManager em;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    InitService init;

    @Test
    public void 상영일정_저장_조회() throws Exception {
        // given
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createBasicMovie();

        // when
        ScreenTime screenTime = new ScreenTime(LocalDateTime.of(2022, 3, 10, 3, 2, 21), movie.getRunningTime());
        Schedule savedSchedule = Schedule.builder()
                .screenTime(screenTime)
                .screen(screen)
                .movie(movie)
                .build();
        em.flush();

        Schedule findSchedule = em.find(Schedule.class, savedSchedule.getId());

        // then
        assertThat(findSchedule).isEqualTo(savedSchedule);
        assertThat(findSchedule.getScreen()).isEqualTo(screen);
        assertThat(findSchedule.getMovie()).isEqualTo(movie);
    }

    @Test
    void 상영일정_전체_좌석_조회() {
        // given
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createBasicMovie();

        Schedule savedSchedule = Schedule.builder()
                .screenTime(new ScreenTime(LocalDateTime.of(2022, 3, 10, 3, 2, 21), movie.getRunningTime()))
                .screen(screen)
                .movie(movie)
                .build();

        // when
        int screenSeatsCount = screen.getMaxCols() * screen.getMaxRows();
        int savedCount = savedSchedule.getTotalSeatCount();
        String savedCountToString = savedSchedule.getTotalSeatCountToString();

        // then
        assertThat(savedCount).isEqualTo(screenSeatsCount);
        assertThat(savedCountToString).isEqualTo(TOTAL + screenSeatsCount + SEAT);
    }

    @Test
    void 상영중인_스케줄_좌석_조회_조회() {
        // given
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createBasicMovie();
        LocalDateTime now = LocalDateTime.now();
        Schedule savedSchedule = Schedule.builder()
                .screenTime(new ScreenTime(now.plusHours(1), movie.getRunningTime()))
                .screen(screen)
                .movie(movie)
                .build();

        // when
        int reservedSeatCount = savedSchedule.getSeatCount(RESERVED);
        int emptySeatCount = savedSchedule.getSeatCount(SeatStatus.EMPTY);
        String savedCountToString = savedSchedule.getReservedSeatValue();

        // then
        assertThat(reservedSeatCount).isEqualTo(0L);
        assertThat(emptySeatCount).isEqualTo(savedSchedule.getTotalSeatCount());
        assertThat(savedCountToString).isEqualTo(reservedSeatCount + SEAT);
    }

    @Test
    void 예매_종료된_스케줄_좌석_조회() {
        // given
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createBasicMovie();

        LocalDateTime now = LocalDateTime.now();
        Schedule savedSchedule = Schedule.builder()
                .screenTime(new ScreenTime(now, movie.getRunningTime()))
                .screen(screen)
                .movie(movie)
                .build();
        em.flush();

        // when
        String savedCountToString = savedSchedule.getReservedSeatValue();

        // then
        assertThat(savedCountToString).isEqualTo(ScheduleStatus.CLOSED.getValue());
    }

    @Test
    void 매진된_스케줄_좌석_조회() {
        // given
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createBasicMovie();

        LocalDateTime now = LocalDateTime.now();
        Schedule savedSchedule = Schedule.builder()
                .screenTime(new ScreenTime(now, movie.getRunningTime()))
                .screen(screen)
                .movie(movie)
                .build();
        em.flush();

        // when
        // 모든 좌석 상태 변경 -> 예매 완료
        savedSchedule.getSeats().forEach(seatEntity -> seatEntity.updateStatus(RESERVED));

        int savedSeatCount = savedSchedule.getSeatCount(RESERVED);
        String savedCountToString = savedSchedule.getReservedSeatValue();


        // then
        assertThat(savedSeatCount).isEqualTo(savedSchedule.getTotalSeatCount());
        assertThat(savedCountToString).isEqualTo(ScheduleStatus.SOLD_OUT.getValue());
    }

}