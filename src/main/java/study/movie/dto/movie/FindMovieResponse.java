package study.movie.dto.movie;

import lombok.Data;
import study.movie.domain.movie.FilmRating;
import study.movie.domain.movie.Movie;

@Data
public class FindMovieResponse {

    private Long id;
    private String image;
    private String title;
    private FilmRating filmRating;
    private Integer releaseDate;

    public FindMovieResponse(Movie movie){
        this.id = movie.getId();
        this.image = movie.getImage();
        this.title = movie.getTitle();
        this.filmRating = movie.getFilmRating();
        this.releaseDate = movie.getReleaseDate().getYear();
    }
}
