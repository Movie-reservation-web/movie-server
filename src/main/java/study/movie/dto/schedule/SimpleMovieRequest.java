package study.movie.dto.schedule;

import lombok.Data;
import study.movie.domain.theater.ScreenFormat;

@Data
public class SimpleMovieRequest {

    private String title;
    private ScreenFormat format;
}
