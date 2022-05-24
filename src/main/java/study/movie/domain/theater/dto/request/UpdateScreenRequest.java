package study.movie.domain.theater.dto.request;

import lombok.Data;
import study.movie.domain.theater.entity.ScreenFormat;

import javax.validation.constraints.NotNull;

@Data
public class UpdateScreenRequest {

    @NotNull
    private ScreenFormat format;
    private Integer maxRows;
    private Integer maxCols;

}
