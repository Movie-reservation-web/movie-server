package study.movie.domain.movie.dto.condition;

import lombok.Data;

@Data
public class ReviewSearchCond {
    private String writer;
    private String movieTitle;
}
