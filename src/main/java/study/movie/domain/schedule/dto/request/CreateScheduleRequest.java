package study.movie.domain.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ApiModel(description = "상영일정 생성 모델")
@Data
public class CreateScheduleRequest {

    @Schema(description = "상영시작날짜,시간", example="yyyy-MM-dd HH:mm:ss",required = true)
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    @Schema(description = "영화 아이디", required = true)
    @NotNull
    private Long movieId;

    @Schema(description = "스크린 아이디", required = true)
    @NotNull
    private Long screenId;
}

