package study.movie.dto.schedule;

import lombok.Data;
import study.movie.domain.movie.Movie;

@Data
public class ScheduleMovieResponse {

    private String movieTitle;
    private String filmRating;
    private String image;

    public ScheduleMovieResponse(Movie movie) {
        this.movieTitle = movie.getTitle();
        this.filmRating = movie.getFilmRating().getValue();
        this.image = movie.getImage();
    }
}
