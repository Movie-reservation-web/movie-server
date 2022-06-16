package study.movie.domain.movie.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import study.movie.domain.movie.entity.Review;

import java.time.LocalDate;

@Getter
@Builder
public class ReviewResponse {

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDate writeDate;
    private String writer;
    private Float score;
    private String comment;

    public static ReviewResponse of(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .writeDate(review.getCreatedDate().toLocalDate())
                .writer(review.getWriter())
                .score(review.getScore())
                .comment(review.getComment())
                .build();
    }
}
