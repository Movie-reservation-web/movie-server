package study.movie.domain.movie.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Data;
import study.movie.domain.movie.entity.FilmFormat;
import study.movie.domain.movie.entity.FilmRating;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.movie.entity.MovieGenre;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateMovieRequest {
    @NotBlank
    private String title;
    private String director;
    private List<String> actors;
    private List<MovieGenre> genres;
    private List<FilmFormat> formats;
    private FilmRating filmRating;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate releaseDate;
    private Integer runningTime;
    private String nation;
    @Lob
    private String intro;
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
                .intro(this.getIntro())
                .image(this.getImage())
                .build();
    }
}
