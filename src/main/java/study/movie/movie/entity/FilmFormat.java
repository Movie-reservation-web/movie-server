package study.movie.movie.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FilmFormat implements EnumMapperType {
    TWO_D("2D"),
    IMAX("IMAX"),
    FOUR_D_FLEX("4DX"),
    SCREEN_X("ScreenX");
    private String value;

    @Override
    public String getCode() {
        return name();
    }
}