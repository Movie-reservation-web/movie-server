package study.movie.domain.theater.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel(description = "상영관 정보 수정 모델")
@Data
public class UpdateTheaterRequest {

    @Schema(description = "전화번호", example = "000-0000-0000", required = true)
    @NotBlank
    @Pattern(regexp = "\\d{3}-\\d{3,4}-\\d{4}")
    private String phone;
}
