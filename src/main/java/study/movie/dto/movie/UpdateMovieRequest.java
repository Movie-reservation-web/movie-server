package study.movie.dto.movie;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import study.movie.domain.movie.FilmRating;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UpdateMovieRequest {

    private Long id;
    private FilmRating filmRating;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    private String info;
    private String image;

}
