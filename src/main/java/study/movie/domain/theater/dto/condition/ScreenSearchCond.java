package study.movie.domain.theater.dto.condition;

import lombok.Data;
import study.movie.domain.theater.entity.ScreenFormat;

@Data
public class ScreenSearchCond {

    private ScreenFormat format;
    private String theaterName;
}
