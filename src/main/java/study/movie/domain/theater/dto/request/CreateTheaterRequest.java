package study.movie.domain.theater.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import study.movie.domain.theater.entity.CityCode;
import study.movie.domain.theater.entity.Theater;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel(description = "상영관 생성 모델")
@Data
public class CreateTheaterRequest {
    @Schema(description = "이름", required = true)
    @NotBlank
    private String name;

    @Schema(description = "도시", required = true)
    @NotBlank
    private CityCode city;

    @Schema(description = "전화번호", example = "000-0000-0000", required = true)
    @NotBlank
    @Pattern(regexp = "\\d{3,4}-\\d{4}")
    private String phone;

    public Theater toEntity() {
        return Theater.builder()
                .name(name)
                .city(city)
                .phone(phone)
                .build();
    }

}
