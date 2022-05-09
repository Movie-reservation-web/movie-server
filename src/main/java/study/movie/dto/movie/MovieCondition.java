package study.movie.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MovieCondition {

    private String director;
    private String title;
    private String actor;

}
