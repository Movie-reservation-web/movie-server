package study.movie.domain.theater.dto.request;

import lombok.Data;
import study.movie.domain.theater.entity.ScreenFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateScreenRequest {
    @NotBlank
    private String name;

    @NotNull
    private ScreenFormat format;

    @Min(0)
    private Integer maxRows;

    @Min(0)
    private Integer maxCols;

    @NotNull
    private Long theaterId;



}
