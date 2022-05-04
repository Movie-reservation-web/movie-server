package study.movie.dto.schedule.response;

import lombok.*;
import study.movie.domain.theater.Theater;

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
