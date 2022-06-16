package study.movie.domain.movie.dto.response;

import lombok.Builder;
import lombok.Data;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.movie.entity.Review;

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
