package study.movie.dto.schedule;

import lombok.Data;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.schedule.Schedule;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ScheduleResponse {

    /**
     * 영화 정보
     */
    private String filmRating;
    private String title;
    private List<String> filmFormats;

    /**
     * 극장 정보
     */
    private String theaterName;
    private String theaterCity;

    /**
     * 예매 날짜 정보
     */
    private LocalDateTime startTime;
    private LocalTime endTime;
    private Integer totalSeatCount;

    public ScheduleResponse(Schedule schedule) {
        this.filmRating = schedule.getMovie().getFilmRating().toString();
        this.title = schedule.getMovie().getTitle();
        this.filmFormats = schedule.getMovie().getFormats().stream().map(FilmFormat::toString).collect(Collectors.toList());
        this.theaterName = schedule.getScreen().getTheater().getName();
        this.theaterCity = schedule.getScreen().getTheater().getCity().toString();
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
        this.totalSeatCount = schedule.getTotalSeatCount();
    }
}
