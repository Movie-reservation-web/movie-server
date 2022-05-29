package study.movie.domain.theater.dto.request;

import lombok.Data;
import study.movie.domain.theater.entity.ScreenFormat;

@Data
public class UpdateScreenRequest {

    private ScreenFormat format;
    private Integer maxRows;
    private Integer maxCols;

}
