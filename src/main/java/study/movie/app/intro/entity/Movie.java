package study.movie.app.intro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import study.movie.app.intro.converter.FilmFormatConverter;
import study.movie.app.intro.converter.MovieGenreConverter;
import study.movie.app.screen.entity.Schedule;
import study.movie.global.converter.StringArrayConverter;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static study.movie.global.constants.EntityAttrConst.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;

    private String title;

    private Integer runningTime;

    private String director;

    // 추후 결정
    @Convert(converter = StringArrayConverter.class)
    private List<String> actors;

    // 추후 결정
    @Convert(converter = MovieGenreConverter.class)
    private List<MovieGenre> genres;

    // 추후 결정
    @Convert(converter = FilmFormatConverter.class)
    private List<FilmFormat> formats;

    private FilmRating filmRating;

    private String nation;

    private LocalDate release;

    @Lob
    private String info;

    private Integer audience;

    private String image;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>();

    //==생성 메서드==//
    @Builder
    public Movie(String title, String director, List<String> actors, List<MovieGenre> genres, FilmRating filmRating, Integer runningTime, String nation, LocalDate release, List<FilmFormat> formats, String info) {
        this.title = title;
        this.director = director;
        this.actors = actors;
        this.genres = genres;
        this.filmRating = filmRating;
        this.runningTime = runningTime;
        this.nation = nation;
        this.release = release;
        this.formats = formats;
        this.info = info;
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
