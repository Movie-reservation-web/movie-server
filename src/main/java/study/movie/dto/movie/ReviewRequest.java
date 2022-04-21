package study.movie.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.Review;

@Data
@AllArgsConstructor
public class ReviewRequest {

    private Long movieId;
    private String writer;
    private Float score;
    private String comment;

}
