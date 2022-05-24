package study.movie.domain.theater.dto.response;

import lombok.Data;
import study.movie.domain.theater.entity.CityCode;
import study.movie.domain.theater.entity.Theater;

@Data
public class BasicTheaterResponse {

    private Long id;
    private String name;
    private CityCode city;
    private String phone;

    public BasicTheaterResponse(Theater theater){
        this.id = theater.getId();
        this.name = theater.getName();
        this.city = theater.getCity();
        this.phone = theater.getPhone();
    }
}
