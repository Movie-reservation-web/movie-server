package study.movie.domain.schedule.dto.response;

import lombok.Builder;
import lombok.Data;
import study.movie.domain.payment.entity.AgeType;

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
