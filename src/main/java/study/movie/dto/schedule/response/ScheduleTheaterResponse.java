package study.movie.dto.schedule.response;

import lombok.Data;
import study.movie.domain.theater.Theater;

@Data
public class ScheduleTheaterResponse {

    private String theaterName;
    private String city;

    public ScheduleTheaterResponse(Theater theater) {
        this.theaterName = theater.getName();
        this.city = theater.getCity().getValue();
    }
}
