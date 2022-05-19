package study.movie.schedule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.validation.constraints.Future;
import java.time.Duration;
import java.time.LocalDateTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"startDateTime", "endDateTime"})
public class ScreenTime {

    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startDateTime;

    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endDateTime;

    public ScreenTime(LocalDateTime startDateTime, int runningTime) {
        this.startDateTime = startDateTime;
        setEndDateTime(runningTime);
    }

    /**
     * 종료 시간 계산하기, 광고 시간은 어디로 놔둘껀지 정해야함.
     */
    public void setEndDateTime(int runningTime) {
        long advTime = 10L;
        endDateTime = startDateTime.plus(Duration.ofMinutes(runningTime + advTime));
    }
}
