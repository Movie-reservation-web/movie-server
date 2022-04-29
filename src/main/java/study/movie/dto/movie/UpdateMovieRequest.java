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
    private FilmRating filmRating;
    private LocalDate releaseDate;
    private String info;
    private String image;

}
