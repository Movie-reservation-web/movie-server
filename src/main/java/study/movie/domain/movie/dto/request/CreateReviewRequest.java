package study.movie.domain.movie.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(description = "리뷰생성 모델")
@Data
public class CreateReviewRequest {

    @Schema(description = "영화 아이디", required = true)
    @NotNull
    private Long movieId;

    @Schema(description = "작성자", required = true)
    @NotBlank
    private String writer;

    @Schema(description = "평점", required = true)
    @NotNull
    @Max(value = 10)
    private Float score;

    @Schema(description = "내용", required = true)
    @NotNull
    private String comment;
}
