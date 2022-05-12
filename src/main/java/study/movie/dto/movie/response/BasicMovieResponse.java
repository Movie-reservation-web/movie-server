package study.movie.dto.movie.response;

import lombok.Builder;
import lombok.Data;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.FilmRating;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.MovieGenre;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
    private LocalDate releaseDate;
    private String info;
    private long audience;
    private String image;

    private List<ReviewResponse> reviews;

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
                .info(movie.getInfo())
                .audience(movie.getAudience())
                .image(movie.getImage())
                .reviews(
                        movie.getReviews().stream()
                                .map(ReviewResponse::of)
                                .collect(Collectors.toList())
                )
                .build();
    }
}