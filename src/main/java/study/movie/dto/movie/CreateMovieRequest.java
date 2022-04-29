package study.movie.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.movie.converter.movie.FilmFormatConverter;
import study.movie.converter.movie.MovieGenreConverter;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.FilmRating;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.MovieGenre;
import study.movie.global.converter.StringArrayConverter;

import javax.persistence.Convert;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class CreateMovieRequest {

    private Long id;
    private String title;
    private Integer runningTime;
    private String director;
    @Convert(converter = StringArrayConverter.class)
    private List<String> actors;
    @Convert(converter = MovieGenreConverter.class)
    private List<MovieGenre> genres;
    @Convert(converter = FilmFormatConverter.class)
    private List<FilmFormat> formats;
    private FilmRating filmRating;
    private String nation;
    private LocalDate releaseDate;
    private String info;
    private Integer audience;
    private String image;
    private String score;

    public CreateMovieRequest(Movie movie) {
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
        this.score = movie.getAverageScore();
    }

    public Movie toEntity() {
        return Movie.builder()
                .title(this.getTitle())
                .director(this.getDirector())
                .actors(this.getActors())
                .genres(this.getGenres())
                .filmRating(this.getFilmRating())
                .runningTime(this.getRunningTime())
                .nation(this.getNation())
                .releaseDate(this.getReleaseDate())
                .formats(this.getFormats())
                .info(this.getInfo())
                .image(this.getImage())
                .build();
    }
}
