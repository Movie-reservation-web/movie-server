package study.movie.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.movie.domain.movie.Movie;

@Data
@AllArgsConstructor
public class UpdateReviewRequest {
    private Long id;
    private Float score;
    private String comment;
}
