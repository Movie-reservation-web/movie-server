package study.movie.dto.theater.request;

import lombok.Data;
import study.movie.domain.theater.CityCode;

import javax.validation.constraints.NotNull;

@Data
public class UpdateTheaterRequest {

    @NotNull
    private Long theaterId;
    private String phone;
}
