package study.movie.domain.movie.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import study.movie.domain.movie.entity.FilmRating;

import java.time.LocalDate;

@Data
public class UpdateMovieRequest {

    private FilmRating filmRating;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    private String info;
    private String image;
}
