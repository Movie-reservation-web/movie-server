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

    private Float score;
    private String comment;

    @Builder
    public Review(Movie movie, Float score, String comment) {
        this.movie = movie;
        this.score = score;
        this.comment = comment;
    }

    /**
     * {@link Movie} Review 생성 메서드
     */
    public static Review writeReview(Movie movie, Float score, String comment) {
        Review review = Review.builder()
                .movie(movie)
                .score(score)
                .comment(comment)
                .build();

        movie.addReview(score);
        return review;
    }

    /**
     * {@link Movie} 비즈니스 로직
     */
    public void delete(){
        getMovie().deleteReview(score);
    }

}
