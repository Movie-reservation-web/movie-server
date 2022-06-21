package study.movie.domain.movie.dto.condition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewSearchCond {
    private String writer;
    private String movieTitle;
}
