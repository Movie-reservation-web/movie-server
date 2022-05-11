package study.movie.dto.movie;

import lombok.Data;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.FilmRating;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.MovieGenre;
import study.movie.global.converter.StringArrayConverter;

import javax.persistence.Convert;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateMovieRequest {

    private Long id;
    private String title;
    private Integer runningTime;
    private String director;
    @Convert(converter = StringArrayConverter.class)
    private List<String> actors;
    private List<MovieGenre> genres;
    private List<FilmFormat> formats;
    private FilmRating filmRating;
    private String nation;
    private LocalDate releaseDate;
    private String info;
    private Integer audience;
    private String image;

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
