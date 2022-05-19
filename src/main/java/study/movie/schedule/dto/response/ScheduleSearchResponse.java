package study.movie.schedule.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import study.movie.schedule.entity.Schedule;

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
@Data
@Builder
public class ScheduleSearchResponse {

    private ScheduleMovieResponse movie;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDate screenDate;
    private ScheduleTheaterResponse theater;

    public static ScheduleSearchResponse of(Schedule schedule) {
        return ScheduleSearchResponse.builder()
                .movie(ScheduleMovieResponse.of(schedule.getMovie()))
                .screenDate(schedule.getScreenTime().getStartDateTime().toLocalDate())
                .theater(ScheduleTheaterResponse.of(schedule.getScreen().getTheater()))
                .build();
    }
}
