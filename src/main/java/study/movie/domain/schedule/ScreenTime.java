package study.movie.domain.schedule;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.movie.domain.movie.Movie;

import javax.persistence.Embeddable;
import javax.validation.constraints.Future;
import java.time.Duration;
import java.time.LocalDateTime;

@Embeddable
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScreenTime {

    @Future
    private LocalDateTime startDateTime;
    @Future
    private LocalDateTime endDateTime;

    public ScreenTime(LocalDateTime startDateTime, Movie movie) {
        this.startDateTime = startDateTime;
        setEndDateTime(movie);
    }

    /**
     * 종료 시간 계산하기, 광고 시간은 어디로 놔둘껀지 정해야함.
     */
    public void setEndDateTime(Movie movie) {
        long advTime = 10L;
        endDateTime = startDateTime.plus(Duration.ofMinutes(movie.getRunningTime()+advTime));
    }
}
