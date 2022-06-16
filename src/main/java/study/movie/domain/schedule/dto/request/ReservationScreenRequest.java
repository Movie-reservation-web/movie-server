package study.movie.domain.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import study.movie.domain.theater.entity.ScreenFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ApiModel(description = "상영관 좌석 가격 정보 가져오는 모델")
@Data
public class ReservationScreenRequest {

    @Schema(description = "상영일정번호", required = true)
    @NotNull
    private String scheduleNumber;

    @Schema(description = "상영관 타입", required = true)
    @NotNull
    private ScreenFormat format;

    @Schema(description = "영화예매 날짜,시간", example="yyyy-MM-dd HH:mm:ss",required = true)
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;
}
