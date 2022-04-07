package study.movie.dto.schedule;

import lombok.Data;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.Theater;

@Data
public class ScheduleTheaterResponse {

    private Long id;
    private String theaterName;
    private CityCode city;

    public ScheduleTheaterResponse(Theater theater) {
        this.id = theater.getId();
        this.theaterName = theater.getName();
        this.city = theater.getCity();
    }
}
