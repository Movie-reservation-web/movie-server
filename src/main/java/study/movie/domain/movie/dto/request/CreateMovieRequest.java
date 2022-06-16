package study.movie.domain.movie.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import study.movie.domain.movie.entity.FilmFormat;
import study.movie.domain.movie.entity.FilmRating;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.movie.entity.MovieGenre;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;


@ApiModel(description = "영화생성 모델")
@Data
public class CreateMovieRequest {
    @Schema(description = "제목", required = true)
    @NotBlank
    private String title;

    @Schema(description = "감독")
    private String director;

    @Schema(description = "배우")
    private List<String> actors;

    @Schema(description = "성별")
    private List<MovieGenre> genres;

    @Schema(description = "상영관유형")
    private List<FilmFormat> formats;

    @Schema(description = "영화관람등급")
    private FilmRating filmRating;

    @Schema(description = "상영날짜",  example = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate releaseDate;

    @Schema(description = "러닝타임")
    private Integer runningTime;

    @Schema(description = "국가")
    private String nation;

    @Schema(description = "줄거리")
    @Lob
    private String intro;

    @Schema(description = "이미지")
    private String image;

    public Movie toEntity() {
        return Movie.builder()
                .title(this.getTitle())
                .director(this.getDirector())
                .actors(this.getActors())
                .genres(this.getGenres())
                .filmRating(this.getFilmRating())
                .runningTime(this.getRunningTime())
                .nation(this.getNation())
                .releaseDate(this.getReleaseDate())
                .formats(this.getFormats())
                .intro(this.getIntro())
                .image(this.getImage())
                .build();
    }
}
