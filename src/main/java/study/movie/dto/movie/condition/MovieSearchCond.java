package study.movie.dto.movie.condition;

import lombok.Data;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.FilmRating;
import study.movie.global.dto.DateRangeCond;

@Data
public class MovieSearchCond extends DateRangeCond {

    private String title;
    private String director;
    private String actor;
    private String nation;
    private FilmRating filmRating;
    private FilmFormat filmFormat;
}
