package study.movie.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.Review;

@Data
@AllArgsConstructor
public class ReviewResponse {

    private Long id;
    private Movie movie;
    private String writer;
    private Float score;
    private String comment;

    public ReviewResponse(Review review) {
        //가능한가?
        this.movie = review.getMovie();
        this.writer = review.getWriter();
        this.score = review.getScore();
        this.comment = review.getComment();
    }
}
