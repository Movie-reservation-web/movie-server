package study.movie.domain.theater.dto.request;

import lombok.Data;
import study.movie.domain.theater.entity.CityCode;
import study.movie.domain.theater.entity.Theater;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CreateTheaterRequest {
    @NotBlank
    private String name;

    @NotBlank
    private CityCode city;

    @NotBlank
    @Pattern(regexp = "\\d{3}-\\d{3,4}-\\d{4}")
    private String phone;

    public Theater toEntity() {
        return Theater.builder()
                .name(name)
                .city(city)
                .phone(phone)
                .build();
    }

}
