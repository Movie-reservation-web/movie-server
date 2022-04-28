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
    private LocalDate releaseDate;
//    private Integer audience;
//    private Long score;
//    //예매율
//    private Long ratings ;

}
