package study.movie.domain.movie.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import study.movie.domain.movie.entity.FilmRating;

import java.time.LocalDate;

@ApiModel(description = "영화 정보 수정 모델")
@Data
public class UpdateMovieRequest {

    @Schema(description = "영화관람등급")
    private FilmRating filmRating;

    @Schema(description = "상영날짜", example = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Schema(description = "줄거리")
    private String info;

    @Schema(description = "이미지")
    private String image;
}
