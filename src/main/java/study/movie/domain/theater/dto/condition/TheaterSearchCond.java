package study.movie.domain.theater.dto.condition;

import lombok.Data;
import study.movie.domain.theater.entity.CityCode;

@Data
public class TheaterSearchCond {

    private String name;
    private CityCode city;
    private String phone;
}
