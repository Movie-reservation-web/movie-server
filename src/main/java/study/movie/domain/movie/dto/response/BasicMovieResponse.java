package study.movie.domain.movie.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import study.movie.domain.movie.entity.FilmFormat;
import study.movie.domain.movie.entity.FilmRating;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.movie.entity.MovieGenre;
import study.movie.global.utils.StringUtil;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDate releaseDate;
    private String info;
    private long audience;
    private String image;
    private String reservationRate;
    public static BasicMovieResponse of(Movie movie) {
        return BasicMovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .runningTime(movie.getRunningTime())
                .director(movie.getDirector())
                .actors(movie.getActors())
                .genres(movie.getGenres())
                .formats(movie.getFormats())
                .filmRating(movie.getFilmRating())
                .nation(movie.getNation())
                .releaseDate(movie.getReleaseDate())
                .info(movie.getIntro())
                .audience(movie.getAudience())
                .image(movie.getImage())
                .reservationRate(StringUtil.convertDoubleToString(movie.getReservationRate()))
                .build();
    }
}
