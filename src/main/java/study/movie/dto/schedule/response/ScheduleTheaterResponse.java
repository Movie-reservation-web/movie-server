package study.movie.dto.schedule.response;

import lombok.*;
import study.movie.domain.theater.Theater;

@Data
@Builder
public class ScheduleTheaterResponse {

    private String theaterName;
    private String city;

    public static ScheduleTheaterResponse of(Theater theater) {
        return ScheduleTheaterResponse.builder()
                .theaterName(theater.getName())
                .city(theater.getCity().getValue())
                .build();
    }
}
