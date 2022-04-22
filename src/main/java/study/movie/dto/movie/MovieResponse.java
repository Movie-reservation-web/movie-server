package study.movie.dto.movie;

import lombok.*;
import study.movie.converter.movie.FilmFormatConverter;
import study.movie.converter.movie.MovieGenreConverter;
import study.movie.domain.movie.*;
import study.movie.domain.schedule.Schedule;
import study.movie.global.converter.StringArrayConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
public class MovieResponse {

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

    private List<Review> reviews = new ArrayList<>();

    private String score;

    public MovieResponse(Movie movie) {
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
        //check
        this.reviews = movie.getReviews();
        this.score = movie.getAverageScore();
    }

}
