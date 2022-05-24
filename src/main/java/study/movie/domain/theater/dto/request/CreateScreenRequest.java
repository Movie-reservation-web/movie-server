package study.movie.domain.theater.dto.request;

import lombok.Data;
import study.movie.domain.theater.entity.ScreenFormat;
import javax.validation.constraints.NotNull;

@Data
@NotNull
public class CreateScreenRequest {

    private String name;
    private ScreenFormat format;
    private Integer maxRows;
    private Integer maxCols;
    private Long theaterId;

}
