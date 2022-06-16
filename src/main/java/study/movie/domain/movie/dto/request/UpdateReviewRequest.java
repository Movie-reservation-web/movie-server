package study.movie.domain.movie.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(description = "리뷰 정보 수정 모델")
@Data
public class UpdateReviewRequest {
    @Schema(description = "평점", required = true)
    @NotNull
    @Max(value = 10)
    private Float score;

    @Schema(description = "내용", required = true)
    @NotBlank
    private String comment;
}
