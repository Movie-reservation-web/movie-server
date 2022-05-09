package study.movie.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.FilmRating;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.MovieGenre;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class BasicMovieResponse {

    private Long id;
    private String title;
    private Integer runningTime;
    private String director;
    private List<String> actors;
    private List<MovieGenre> genres;
    private List<FilmFormat> formats;
    private FilmRating filmRating;
    private String nation;
    private LocalDate releaseDate;
    private String info;
    private Integer audience;
    private String image;

    private List<ReviewResponse> reviews;

    public BasicMovieResponse(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.runningTime = movie.getRunningTime();
        this.director = movie.getDirector();
        this.actors = movie.getActors();
        this.genres = movie.getGenres();
        this.formats = movie.getFormats();
        this.filmRating = movie.getFilmRating();
        this.nation = movie.getNation();
        this.releaseDate = movie.getReleaseDate();
        this.info = movie.getInfo();
        this.audience = movie.getAudience();
        this.image = movie.getImage();
        this.reviews = movie.getReviews().stream().map(ReviewResponse::new).collect(Collectors.toList());
    }
}
