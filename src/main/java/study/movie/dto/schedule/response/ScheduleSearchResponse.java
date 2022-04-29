package study.movie.dto.schedule.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import study.movie.domain.schedule.Schedule;

import java.time.LocalDate;

/**
 * 영화 예매 -> 상영일정 검색 시 사용
 * <p>
 * - 영화 정보(영화 제목, 등급, 이미지)
 * <p>
 * - 극장 정보(도시, 극장 이름)
 * <p>
 * - 상영 날짜
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleSearchResponse extends BaseScheduleResponse {

    private ScheduleMovieResponse movie;
    private LocalDate screenDate;
    private ScheduleTheaterResponse theater;

    public ScheduleSearchResponse(Schedule schedule) {
        this.movie = new ScheduleMovieResponse(schedule.getMovie());
        this.screenDate = schedule.getScreenTime().getStartDateTime().toLocalDate();
        this.theater = new ScheduleTheaterResponse(schedule.getScreen().getTheater());
    }
}
