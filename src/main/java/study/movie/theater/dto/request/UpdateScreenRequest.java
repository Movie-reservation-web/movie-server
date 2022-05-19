package study.movie.theater.dto.request;

import lombok.Data;
import study.movie.theater.entity.ScreenFormat;

import javax.validation.constraints.NotNull;

@Data
public class UpdateScreenRequest {

    @NotNull
    private ScreenFormat format;
    private Integer maxRows;
    private Integer maxCols;

}
