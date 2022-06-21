package study.movie.domain.theater.dto.condition;

import lombok.Getter;
import lombok.Setter;
import study.movie.domain.theater.entity.CityCode;

@Getter
@Setter
public class TheaterSearchCond {

    private String name;
    private CityCode city;
    private String phone;
}
