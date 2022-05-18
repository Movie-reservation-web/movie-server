package study.movie.dto.theater.request;

import lombok.Data;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.Theater;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@NotNull
public class CreateTheaterRequest {

    private String name;
    private CityCode city;
    private String phone;

    public Theater toEntity() {

    }
}
