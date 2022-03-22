package study.movie.domain.movie;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FilmFormat implements EnumMapperType {
    THREE_D( "3D"),
    IMAX( "IMAX"),
    FOUR_D_FLEX( "4DX"),
    SCREEN_X( "ScreenX"),
    FOUR_D_FLEX_SCREEN("4DX-Screen");
    private String value;

    @Override
    public String getCode() {
        return name();
    }
}