package study.movie.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.movie.domain.movie.Movie;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class FindMovieResponse {

    Long id;
    String image;
    String title;
    LocalDate releaseDate;

    public FindMovieResponse(Movie movie){
        this.id = movie.getId();
        this.image = movie.getImage();
        this.title = movie.getTitle();
        this.releaseDate = movie.getReleaseDate();
    }
}
