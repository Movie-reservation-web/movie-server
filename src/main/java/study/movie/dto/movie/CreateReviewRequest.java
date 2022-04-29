package study.movie.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.Review;

@Data
@AllArgsConstructor
public class CreateReviewRequest {

    private Long id;
    private Long movieId;
    private String writer;
    private Float score;
    private String comment;

    public Review toEntity(Movie findMovie) {
        return Review.builder()
                .movie(findMovie)
                .writer(this.getWriter())
                .score(this.getScore())
                .comment(this.getComment())
                .build();
    }


}
