package study.movie.dto.theater.request;

import lombok.Data;
import study.movie.domain.theater.ScreenFormat;

import javax.validation.constraints.NotNull;

@Data
public class UpdateScreenRequest {

    @NotNull
    private Long ScreenId;
    private ScreenFormat format;
    private Integer maxRows;
    private Integer maxCols;

}
