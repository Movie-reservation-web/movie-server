package study.movie.domain.theater.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import study.movie.domain.theater.entity.ScreenFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(description = "스크린 생성 모델")
@Data
public class CreateScreenRequest {
    @Schema(description = "이름", required = true)
    @NotBlank
    private String name;

    @Schema(description = "타입", required = true)
    @NotNull
    private ScreenFormat format;

    @Schema(description = "최대 행 수", defaultValue = "0", required = true)
    @Min(0)
    private Integer maxRows;

    @Schema(description = "최대 열 수", defaultValue = "0", required = true)
    @Min(0)
    private Integer maxCols;

    @Schema(description = "상영관 아이디", required = true)
    @NotNull
    private Long theaterId;



}
