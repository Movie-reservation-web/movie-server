package study.movie.domain.movie.dto.condition;

import lombok.Getter;
import lombok.Setter;
import study.movie.domain.movie.entity.FilmFormat;
import study.movie.domain.movie.entity.FilmRating;
import study.movie.global.dto.DateRangeCond;

@Getter
@Setter
public class MovieSearchCond extends DateRangeCond {
    private String title;
    private String director;
    private String actor;
    private String nation;
    private FilmRating filmRating;
    private FilmFormat filmFormat;
}
