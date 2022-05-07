package study.movie.dto.schedule.request;

import lombok.Builder;
import lombok.Data;
import study.movie.domain.schedule.Seat;
import study.movie.domain.schedule.SeatStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class UpdateSeatRequest {
    @NotNull
    private Long scheduleId;

    @NotNull
    private List<Seat> seats;

    @NotNull
    private SeatStatus status;

    public static UpdateSeatRequest from(Long scheduleId, List<String> seats, SeatStatus status) {
        return UpdateSeatRequest.builder()
                .scheduleId(scheduleId)
                .seats(
                        seats.stream().map(Seat::stringToSeat).collect(Collectors.toList())
                )
                .status(status)
                .build();
    }

}
