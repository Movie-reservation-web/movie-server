package study.movie.domain.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import study.movie.domain.theater.entity.Theater;

@Data
@Builder
public class ScheduleTheaterResponse {
    private Long id;
    private String theaterName;
    private String city;

    public static ScheduleTheaterResponse of(Theater theater) {
        return ScheduleTheaterResponse.builder()
                .id(theater.getId())
                .theaterName(theater.getName())
                .city(theater.getCity().getValue())
                .build();
    }
}
