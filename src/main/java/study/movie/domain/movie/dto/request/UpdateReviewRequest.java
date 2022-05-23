package study.movie.domain.movie.dto.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateReviewRequest {
    @NotNull
    @Max(value = 10)
    private Float score;
    @NotBlank
    private String comment;
}
