package study.movie.dto.schedule;

import lombok.Data;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.FilmRating;
import study.movie.domain.movie.Movie;

import java.util.List;

@Data
public class ScheduleMovieResponse {

    private Long id;
    private String movieTitle;
    private FilmRating filmRating;
    private List<FilmFormat> filmFormat;

    public ScheduleMovieResponse(Movie movie) {
        this.id = movie.getId();
        this.movieTitle = movie.getTitle();
        this.filmRating = movie.getFilmRating();
        this.filmFormat = movie.getFormats();
    }
}
