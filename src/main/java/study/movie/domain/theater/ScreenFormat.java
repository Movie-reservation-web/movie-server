package study.movie.domain.theater;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.domain.movie.FilmFormat;
import study.movie.global.enumMapper.EnumMapperType;

import java.util.Set;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ScreenFormat implements EnumMapperType {
    NORMAL("일반", Set.of(FilmFormat.TWO_D)),
    IMAX("IMAX", Set.of(FilmFormat.IMAX)),
    FOUR_D_FLEX("4DX", Set.of(FilmFormat.FOUR_D_FLEX)),
    SCREEN_X("ScreenX", Set.of(FilmFormat.SCREEN_X)),
    FOUR_D_FLEX_SCREEN("4DX SCREEN", Set.of(FilmFormat.FOUR_D_FLEX, FilmFormat.SCREEN_X)),
    CINE_DE_CHEF("CINE de CHEF", Set.of(FilmFormat.TWO_D)),
    SWEET_BOX("SWEET BOX", Set.of(FilmFormat.TWO_D));
    private String value;
    private Set<FilmFormat> filmFormats;

    @Override
    public String getCode() {
        return name();
    }

}