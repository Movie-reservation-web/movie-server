package study.movie.domain.review;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.movie.Movie;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Review {

    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false)
    private String reservedId;

    @Column(nullable = false)
    private Long score;

    @Column(nullable = false)
    private String review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Builder
    public Review(String reservedId, Long score, String review, Movie movie) {
        this.reservedId = reservedId;
        this.score = score;
        this.review = review;
        addReview(movie);
    }

    public void addReview(Movie movie) {
        if (movie == null) {
            return;
        }
        this.movie = movie;
        movie.getReviews().add(this);
    }
}
