package study.movie.movie.dto.condition;

import lombok.Data;
import study.movie.movie.entity.FilmFormat;
import study.movie.movie.entity.FilmRating;
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
