package study.movie.dto.movie.response;

import lombok.Builder;
import lombok.Data;
import study.movie.domain.movie.FilmRating;
import study.movie.domain.movie.Movie;

@Data
@Builder
public class FindMovieResponse {

    private Long id;
    private String image;
    private String title;
    private FilmRating filmRating;
    private Integer releaseDate;

    public static FindMovieResponse of(Movie movie) {
        return FindMovieResponse.builder()
                .id(movie.getId())
                .image(movie.getImage())
                .title(movie.getTitle())
                .filmRating(movie.getFilmRating())
                .releaseDate(movie.getReleaseDate().getYear())
                .build();
    }
}
