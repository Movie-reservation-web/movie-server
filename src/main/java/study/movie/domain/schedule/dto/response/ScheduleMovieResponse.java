package study.movie.domain.schedule.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import study.movie.domain.movie.entity.Movie;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class ScheduleMovieResponse {

    private Long id;
    private String movieTitle;
    private String filmRating;
    private String image;

    public static ScheduleMovieResponse of(Movie movie) {
        return ScheduleMovieResponse.builder()
                .id(movie.getId())
                .movieTitle(movie.getTitle())
                .filmRating(movie.getFilmRating().getValue())
                .image(movie.getImage())
                .build();
    }

    @ApiModel(description = "티켓 예약 정보 모델")
    @Data
    public static class ReserveTicketRequest {
        @Schema(description = "상품 아이디", required = true)
        @NotNull
        @JsonProperty("merchant_uid")
        private String merchantUid;

        @Schema(description = "상영일정 번호", required = true)
        @NotNull
        @JsonProperty("schedule_number")
        private String scheduleNumber;

        @Schema(description = "회원 이메일", required = true)
        @NotNull
        @JsonProperty("member_email")
        private String memberEmail;

        @Schema(description = "좌석", required = true)
        @NotNull
        private List<String> seats;
    }
}
