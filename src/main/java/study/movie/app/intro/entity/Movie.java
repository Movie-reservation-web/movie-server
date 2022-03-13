package study.movie.app.intro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.app.intro.converter.FilmFormatConverter;
import study.movie.app.reserve.entity.Ticket;
import study.movie.app.screen.entity.Schedule;
import study.movie.global.converter.StringArrayConverter;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
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

    private Integer runningTime;

    private String nation;

    private LocalDate release;

    @Convert(converter = FilmFormatConverter.class)
    private List<FilmFormat> formats;

    @Lob
    private String intro;

    private Integer audience;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>();

    //==생성 메서드==//
    @Builder
    public Movie(String title, String director, List<String> actors, Genre genre, FilmRating filmRating, Integer runningTime, String nation, LocalDate release, List<FilmFormat> formats, String intro) {
        this.title = title;
        this.director = director;
        this.actors = actors;
        this.genre = genre;
        this.filmRating = filmRating;
        this.runningTime = runningTime;
        this.nation = nation;
        this.release = release;
        this.formats = formats;
        this.intro = intro;
        this.audience = 0;
    }

    //== 비즈니스 로직==//
    /**
     * 리뷰 개수
     */
    public int getReviewCount() {
        return reviews.size();
    }

    /**
     * 리뷰 삭제
     */
    public void deleteReview(Review review) {
        reviews.remove(review);
    }

    /**
     * 평점 계산
     */
    private String getAverageScore() {
        return String.format("%.2f", reviews.stream()
                .mapToDouble(Review::getScore)
                .average().getAsDouble());
    }
}
