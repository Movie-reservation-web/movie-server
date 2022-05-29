package study.movie.domain.theater.dto.condition;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ScreenSearchType implements EnumMapperType {
    FORMAT("상영관 타입"),
    THEATER_NAME("영화관 이름");
    private String value;

    @Override
    public String getCode() {
        return name();
    }
}
