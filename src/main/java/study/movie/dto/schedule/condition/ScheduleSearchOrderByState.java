package study.movie.dto.schedule.condition;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ScheduleSearchOrderByState implements EnumMapperType {
    MOVIE_TITLE("영화 제목","movieTitle"),
    SCREEN_DATE("상영 시간","screenDate"),
    THEATER_NAME("극장 이름","theaterName"),
    AUDIENCE_COUNT("관객 수","audience");

    private String title;
    private String value;

    @Override
    public String getCode() {
        return name();
    }
}
