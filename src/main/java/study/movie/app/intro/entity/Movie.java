package study.movie.app.intro.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.app.intro.converter.FilmFormatConverter;
import study.movie.global.converter.StringArrayConverter;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static study.movie.global.constants.EntityAttrConst.*;

@Entity
@Getter
@Table(name = "movies")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;

    private String title;
    private String director;

    @Convert(converter = StringArrayConverter.class)
    private List<String> actors;

    private Genre genre;

    private FilmRating filmRating;

    private Integer screenTime;

    private String nation;

    private LocalDate release;

    @Convert(converter = FilmFormatConverter.class)
    private List<FilmFormat> formats;

    private String intro;

    private Integer audience;

    private Float score;

    private Integer reviewQuantity;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

    //==생성 메서드==//
    @Builder
    public Movie(String title, String director, List<String> actors, Genre genre, FilmRating filmRating, Integer screenTime, String nation, LocalDate release, List<FilmFormat> formats, String intro) {
        this.title = title;
        this.director = director;
        this.actors = actors;
        this.genre = genre;
        this.filmRating = filmRating;
        this.screenTime = screenTime;
        this.nation = nation;
        this.release = release;
        this.formats = formats;
        this.intro = intro;
        this.audience = 0;
        this.score = null;
        this.reviewQuantity = 0;
    }

    //== 비즈니스 로직==//
    /**
     * 리뷰 추가
     */
    public void addReview(Float score) {
        float totalScore = this.score * reviewQuantity;
        reviewQuantity++;
        applyScore(totalScore + score);
    }

    /**
     * 리뷰 삭제
     */
    public void deleteReview(Float score) {
        float totalScore = this.score * reviewQuantity;
        reviewQuantity--;
        applyScore(totalScore - score);
    }

    /**
     * 평점 계산
     */
    private void applyScore(Float totalScore) {
        if(reviewQuantity == 0) this.score = null;
        else this.score = totalScore / reviewQuantity;
    }
}
