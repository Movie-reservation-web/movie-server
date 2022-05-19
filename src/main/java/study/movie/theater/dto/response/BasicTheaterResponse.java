package study.movie.theater.dto.response;

import lombok.Data;
import study.movie.theater.entity.CityCode;
import study.movie.theater.entity.Theater;

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
