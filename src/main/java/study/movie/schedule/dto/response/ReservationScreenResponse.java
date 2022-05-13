package study.movie.schedule.dto.response;

import lombok.Builder;
import lombok.Data;
import study.movie.ticket.entity.payment.AgeType;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ReservationScreenResponse {

    private List<SeatResponse> seats;
    private Map<AgeType, Integer> priceMap;

    public static ReservationScreenResponse of(List<SeatResponse> seats, Map<AgeType, Integer> priceMap) {
        return ReservationScreenResponse.builder()
                .seats(seats)
                .priceMap(priceMap)
                .build();
    }
}
