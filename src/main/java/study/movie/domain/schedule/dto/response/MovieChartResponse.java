package study.movie.domain.schedule.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import study.movie.domain.movie.entity.FilmRating;
import study.movie.domain.movie.entity.Movie;

import java.time.LocalDate;

@Data
@Builder
public class MovieChartResponse {
    private Long id;
    private String title;
    private FilmRating filmRating;
    private String image;
    private String reservationRate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDate releaseDate;

    public static MovieChartResponse of(Movie movie, double totalViewCount){
        return MovieChartResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .image(movie.getImage())
                .filmRating(movie.getFilmRating())
                .releaseDate(movie.getReleaseDate())
                .reservationRate(String.format("%.1f", (double) movie.getAudience() / totalViewCount * 100.0) + "%")
                .build();

    }
}
