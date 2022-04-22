package study.movie.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.movie.converter.movie.FilmFormatConverter;
import study.movie.converter.movie.MovieGenreConverter;
import study.movie.domain.movie.*;
import study.movie.domain.schedule.Schedule;
import study.movie.global.converter.StringArrayConverter;

import javax.persistence.Convert;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class UpdateMovieRequest {

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
    private List<Review> reviews;

}
