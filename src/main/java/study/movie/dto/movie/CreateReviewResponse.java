package study.movie.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.Review;

@Data
@AllArgsConstructor
public class CreateReviewResponse {

    private Long id;
    private Movie movie;
    private String writer;
    private Float score;
    private String comment;

    public CreateReviewResponse(Review review) {
        this.movie = review.getMovie();
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
