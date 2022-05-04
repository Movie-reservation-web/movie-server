package study.movie.dto.schedule.response;

import lombok.*;
import study.movie.domain.movie.Movie;

@Data
@Builder
public class ScheduleMovieResponse {

    private Long id;
    private String movieTitle;
    private String filmRating;
    private String image;

    public static ScheduleMovieResponse of(Movie movie) {
        return ScheduleMovieResponse.builder()
                .id(movie.getId())
                .movieTitle(movie.getTitle())
                .filmRating(movie.getFilmRating().getValue())
                .image(movie.getImage())
                .build();
    }
}
