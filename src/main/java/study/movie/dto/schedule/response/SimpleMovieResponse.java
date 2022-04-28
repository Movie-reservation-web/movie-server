package study.movie.dto.schedule.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import study.movie.domain.movie.FilmRating;
import study.movie.domain.movie.Movie;

import java.time.LocalDate;

@Data
@Builder
public class SimpleMovieResponse {
    private Long id;
    private String title;
    private FilmRating filmRating;
    private String image;
    private String reservationRate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDate releaseDate;

    public static SimpleMovieResponse of(Movie movie, double totalViewCount){
        return SimpleMovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .image(movie.getImage())
                .filmRating(movie.getFilmRating())
                .releaseDate(movie.getReleaseDate())
                .reservationRate(String.format("%.1f", (double) movie.getAudience() / totalViewCount * 100.0) + "%")
                .build();

    }
}
