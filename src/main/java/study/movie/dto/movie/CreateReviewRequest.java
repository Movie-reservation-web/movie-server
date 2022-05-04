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

    public CreateReviewRequest(Review review, Long movieId) {
        this.movieId = movieId;
        this.writer = review.getWriter();
        this.score = review.getScore();
        this.comment = review.getComment();
    }

    public Review toEntity(Movie findMovie) {
        return Review.builder()
                .movie(findMovie)
                .writer(this.getWriter())
                .score(this.getScore())
                .comment(this.getComment())
                .build();
    }


}
