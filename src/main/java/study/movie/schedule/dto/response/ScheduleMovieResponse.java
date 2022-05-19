package study.movie.schedule.dto.response;

import lombok.*;
import study.movie.movie.entity.Movie;

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
