package study.movie.domain.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import study.movie.domain.theater.entity.ScreenFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ApiModel(description = "상영일정 상영관 정보 모델")
@Data
public class ScheduleScreenRequest {

    @Schema(description = "상영 날짜,시간", example="yyyy-MM-dd HH:mm:ss",required = true)
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate screenDate;

    @Schema(description = "영화 아이디", required = true)
    @NotNull
    private Long movieId;

    @Schema(description = "상영관 타입", required = true)
    @NotBlank
    private ScreenFormat screenFormat;

    @Schema(description = "극장 아이디", required = true)
    @NotNull
    private Long theaterId;
}
