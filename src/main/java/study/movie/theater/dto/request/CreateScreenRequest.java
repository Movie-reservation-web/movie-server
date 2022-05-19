package study.movie.theater.dto.request;

import lombok.Data;
import study.movie.theater.entity.ScreenFormat;

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
