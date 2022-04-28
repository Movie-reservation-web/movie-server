package study.movie.domain.schedule;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.Movie;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.Screen;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.theater.Theater;
import study.movie.repository.schedule.ScheduleRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static study.movie.domain.schedule.SeatStatus.RESERVED;
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

    @Autowired
    InitService init;

    @Test
    public void 상영일정_저장_조회() throws Exception {
        // given
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));

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
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));

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
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));
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
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));

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
        Movie movie = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D));

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