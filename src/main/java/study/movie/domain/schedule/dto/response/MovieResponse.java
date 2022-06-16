package study.movie.domain.schedule.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import study.movie.domain.movie.entity.FilmFormat;
import study.movie.domain.movie.entity.FilmRating;
import study.movie.domain.movie.entity.Movie;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class MovieResponse {

    private Long id;
    private String title;
    private FilmRating filmRating;
    private List<FilmFormat> filmFormats;
    private String image;
    private String reservationRate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDate releaseDate;

    public static MovieResponse of(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .image(movie.getImage())
                .filmFormats(movie.getFormats())
                .filmRating(movie.getFilmRating())
                .releaseDate(movie.getReleaseDate())
                .build();
    }
}
