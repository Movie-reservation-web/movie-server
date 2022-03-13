package study.movie.app.intro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private String reserveNumber;

    private String writer;
    private Float score;
    private String comment;

    @Builder
    public Review(String writer, Float score, String comment) {
        this.writer = writer;
        this.score = score;
        this.comment = comment;
    }

    //==연관 관계 메서드==//
    public void write(Movie movie) {
        this.movie = movie;
        movie.getReviews().add(this);
    }

    //==생성 메서드==//
    public static Review createReview(Movie movie, String writer, Float score, String comment) {
        Review review = Review.builder()
                .writer(writer)
                .score(score)
                .comment(comment)
                .build();

        review.write(movie);
        return review;
    }

}
