package study.movie.dto.schedule;

import lombok.Data;
import study.movie.domain.theater.CityCode;

@Data
public class SimpleTheaterRequest {

    private CityCode city;
    private String theaterName;
}
