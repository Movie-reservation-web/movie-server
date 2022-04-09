package study.movie.domain.theater;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.domain.movie.FilmFormat;
import study.movie.global.enumMapper.EnumMapperType;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ScreenFormat implements EnumMapperType {
    TWO_D("2D", FilmFormat.TWO_D),
    THREE_D("3D",FilmFormat.THREE_D),
    IMAX("IMAX", FilmFormat.IMAX),
    FOUR_D_FLEX("4DX", FilmFormat.FOUR_D_FLEX),
    SCREEN_X("ScreenX", FilmFormat.SCREEN_X),
    FOUR_D_FLEX_SCREEN("4DX-Screen", FilmFormat.FOUR_D_FLEX_SCREEN),
    SUITE_CINEMA("Suite Cinema", FilmFormat.TWO_D),
    CINE_DE_CHEF("CINE de CHEF", FilmFormat.TWO_D),
    GOLD_CLASS("GOLD Class", FilmFormat.TWO_D),
    SKY_BOX("SKY BOX", FilmFormat.TWO_D),
    CINE_KIDS("CINE Kids", FilmFormat.TWO_D),
    SPHERE_FLEX("SPHERE X", FilmFormat.TWO_D),
    SOUND_FLEX("SOUND X", FilmFormat.TWO_D),
    PREMIUM("PREMIUM", FilmFormat.TWO_D);
    private String value;
    private FilmFormat filmFormat;
    @Override
    public String getCode() {
        return name();
    }

    public FilmFormat getFilmFormat() {
        return filmFormat;
    }
}