package study.movie.domain.schedule.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.schedule.repository.ScheduleRepository;
import study.movie.domain.theater.entity.Screen;
import study.movie.domain.theater.entity.Theater;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static study.movie.domain.schedule.entity.SeatStatus.RESERVED;
import static study.movie.global.constants.StringAttrConst.SEAT;
import static study.movie.global.constants.StringAttrConst.TOTAL;

@SpringBootTest
@Transactional
@Slf4j
@Rollback
class ScheduleTest {

    @Autowired
    EntityManager em;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Test
    void 상영일정_전체_좌석_조회() {
        // given
        Theater theater = em.find(Theater.class,1L);
        Screen screen = theater.getScreens().get(0);
        Movie movie = em.find(Movie.class, 1L);

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
        Theater theater = em.find(Theater.class,1L);
        Screen screen = theater.getScreens().get(0);
        Movie movie = em.find(Movie.class, 1L);
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
        Theater theater = em.find(Theater.class,1L);
        Screen screen = theater.getScreens().get(0);
        Movie movie = em.find(Movie.class, 1L);

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
        Theater theater = em.find(Theater.class,1L);
        Screen screen = theater.getScreens().get(0);
        Movie movie = em.find(Movie.class, 1L);

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
