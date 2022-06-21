package study.movie.domain.theater.dto.condition;

import lombok.Getter;
import lombok.Setter;
import study.movie.domain.theater.entity.ScreenFormat;

@Getter
@Setter
public class ScreenSearchCond {

    private ScreenFormat format;
    private String theaterName;
}
