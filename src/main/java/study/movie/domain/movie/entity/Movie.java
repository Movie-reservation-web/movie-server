package study.movie.domain.movie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.movie.converter.FilmFormatConverter;
import study.movie.domain.movie.converter.MovieGenreConverter;
import study.movie.domain.schedule.entity.Schedule;
import study.movie.domain.ticket.entity.Ticket;
import study.movie.global.converter.StringArrayConverter;
import study.movie.global.entity.BaseTimeEntity;
import study.movie.exception.CustomException;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static study.movie.exception.ErrorCode.NEGATIVE_AUDIENCE_COUNT;

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

    @Convert(converter = StringArrayConverter.class)
    private List<String> actors;

    @Convert(converter = MovieGenreConverter.class)
    private List<MovieGenre> genres;

    @Convert(converter = FilmFormatConverter.class)
    private List<FilmFormat> formats;

    private FilmRating filmRating;

    private String nation;

    private LocalDate releaseDate;

    @Lob
    private String info;

    private long audience;

    private String image;

    private double avgScore;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST)
    private List<Review> reviews = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "movie")
    private List<Schedule> schedules = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "movie")
    private List<Ticket> tickets = new ArrayList<>();

    //==생성 메서드==//
    @Builder
    public Movie(String title, String director, List<String> actors, List<MovieGenre> genres, FilmRating filmRating, Integer runningTime, String nation, LocalDate releaseDate, List<FilmFormat> formats, String info, String image) {
        this.title = title;
        this.director = director;
        this.actors = actors;
        this.genres = genres;
        this.filmRating = filmRating;
        this.runningTime = runningTime;
        this.nation = nation;
        this.releaseDate = releaseDate;
        this.formats = formats;
        this.info = info;
        this.image = image;
        this.audience = 0L;
        this.avgScore = 0F;
    }

    //== 비즈니스 로직==//
    /**
     * 평점 계산
     */
    public void calcAverageScore() {
        this.avgScore = reviews.stream().
                mapToDouble(Review::getScore)
                .average().getAsDouble();
    }

    public String getAvgScoreToString() {
        return String.format("%.1f", avgScore);
    }

    public void update(FilmRating filmRating, LocalDate releaseDate, String info, String image) {
        this.updateFilmRating(filmRating);
        this.updateReleaseDate(releaseDate);
        this.updateInfo(info);
        this.updateImage(image);
    }

    private void updateReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    private void updateFilmRating(FilmRating filmRating) {
        this.filmRating = filmRating;
    }

    private void updateInfo(String info) {
        this.info = info;
    }

    private void updateImage(String image) {
        this.image = image;
    }

    public void addAudience(int count) {
        audience += count;
    }

    public void dropAudience(int count) {
        if(audience < count) throw new CustomException(NEGATIVE_AUDIENCE_COUNT);
        audience -= count;
    }

    //== 조회 로직==//
    /**
     * 리뷰 개수
     */
    public long getReviewCount() {
        return reviews.size();
    }

}
