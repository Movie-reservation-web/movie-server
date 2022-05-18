package study.movie.dto.theater.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.Theater;

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
