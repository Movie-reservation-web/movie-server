package study.movie.theater.dto.request;

import lombok.Data;
import study.movie.theater.entity.CityCode;
import javax.validation.constraints.NotNull;

@Data
@NotNull
public class CreateTheaterRequest {

    private String name;
    private CityCode city;
    private String phone;

}
