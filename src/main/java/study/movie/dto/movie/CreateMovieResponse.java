package study.movie.dto.movie;

import lombok.*;
import study.movie.converter.movie.FilmFormatConverter;
import study.movie.converter.movie.MovieGenreConverter;
import study.movie.domain.movie.*;
import study.movie.global.converter.StringArrayConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class CreateMovieResponse {

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

    public CreateMovieResponse(Movie movie) {
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
    }
}
