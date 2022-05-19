package study.movie.movie.dto.response;

import lombok.Builder;
import lombok.Data;
import study.movie.movie.entity.Movie;
import study.movie.movie.entity.Review;

@Data
@Builder
public class ReviewResponse {

    private Long id;
    private Movie movie;
    private String writer;
    private Float score;
    private String comment;

    public static ReviewResponse of(Review review) {
        return ReviewResponse.builder()
                .movie(review.getMovie())
                .writer(review.getWriter())
                .score(review.getScore())
                .comment(review.getComment())
                .build();
    }
}
