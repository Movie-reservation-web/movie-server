package study.movie.domain.schedule.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import study.movie.domain.schedule.entity.Seat;
import study.movie.domain.schedule.entity.SeatStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel(description = "좌석 정보 수정 모델")
@Data
@Builder
public class UpdateSeatRequest {
    @Schema(description = "상영일정 아이디", required = true)
    @NotNull
    private Long scheduleId;

    @Schema(description = "좌석", required = true)
    @NotNull
    private List<Seat> seats;

    @Schema(description = "상태", required = true)
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
