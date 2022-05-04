package study.movie.domain.seat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.screening.Screening;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
class SeatRepositoryTest {

//    @Autowired
//    SeatRepository seatRepository;
//
//    @Autowired
//    ScreeningRepository screeningRepository;

//    @Test
//    void 좌석을_조회한다() {
//        //given
//        Screening screening = Screening.builder()
//                .startTime(LocalDateTime.now())
//                .build();
//        Screening savedScreening = screeningRepository.save(screening);
//
//        Seat seat = Seat.builder()
//                .seatRow(4L)
//                .seatColumn(5L)
//                .screening(screening)
//                .seatStatus(SeatStatus.RESERVED)
//                .build();
//        Seat savedSeat = seatRepository.save(seat);
//
//        //when
//        Seat findSeat = seatRepository.findById(savedSeat.getId()).get();
//
//        //then
//        Assertions.assertThat(findSeat).isEqualTo(savedSeat);
//    }

}