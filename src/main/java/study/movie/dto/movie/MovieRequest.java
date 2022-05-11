package study.movie.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.movie.domain.movie.*;
import study.movie.domain.schedule.Schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class MovieRequest {

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

    private List<Review> reviews = new ArrayList<>();
    private List<Schedule> schedules = new ArrayList<>();

    public MovieRequest(Movie moive) {
        this.title = moive.getTitle();
        this.runningTime = moive.getRunningTime();
        this.director = moive.getDirector();
        this.actors = moive.getActors();
        this.genres = moive.getGenres();
        this.formats = moive.getFormats();
        this.filmRating = moive.getFilmRating();
        this.nation = moive.getNation();
        this.releaseDate = moive.getReleaseDate();
        this.info = moive.getInfo();
        this.audience = moive.getAudience();
        this.image = moive.getImage();
        //check
        this.reviews = moive.getReviews();
        this.schedules = moive.getSchedules();
    }



}
