package study.movie.domain.theater.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import study.movie.domain.theater.entity.ScreenFormat;

@ApiModel(description = "스크린 정보 수정 모델")
@Data
public class UpdateScreenRequest {
    @Schema(description = "포멧")
    private ScreenFormat format;

    @Schema(description = "최대 행 수")
    private Integer maxRows;

    @Schema(description = "최대 열 수")
    private Integer maxCols;

}
