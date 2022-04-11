package study.movie.dto.schedule;

import lombok.Data;
import study.movie.domain.schedule.SeatStatus;
import study.movie.domain.schedule.Seat;

import java.util.List;

@Data
public class UpdateSeatCond {
    private Long scheduleId;
    private List<Seat> seats;
    private SeatStatus status;
}
