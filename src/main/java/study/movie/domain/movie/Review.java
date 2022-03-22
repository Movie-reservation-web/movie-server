package study.movie.domain.movie;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private String writer;
    private Float score;
    private String comment;

    //==생성 메서드==//
    @Builder
    public Review(Movie movie, String writer, Float score, String comment) {
        this.writer = writer;
        this.score = score;
        this.comment = comment;
        if(movie != null) write(movie);
    }

    //==연관 관계 메서드==//
    public void write(Movie movie) {
        this.movie = movie;
        movie.getReviews().add(this);
    }
}
