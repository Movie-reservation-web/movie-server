package study.movie.dto.movie.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateReviewRequest {

    @NotNull
    private Long movieId;

    @NotBlank
    private String writer;

    @NotNull
    @Max(value = 10)
    private Float score;


    @NotNull
    private String comment;
}
