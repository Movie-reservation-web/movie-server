package study.movie.domain.theater;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ScreenFormat implements EnumMapperType {
    THREE_D("3D"),
    IMAX("IMAX"),
    FOUR_D_FLEX("4DX"),
    SCREEN_X("ScreenX"),
    FOUR_D_FLEX_SCREEN("4DX-Screen"),
    SUITE_CINEMA("Suite Cinema"),
    CINE_DE_CHEF("CINE de CHEF"),
    GOLD_CLASS("GOLD Class"),
    SKY_BOX("SKY BOX"),
    CINE_KIDS("CINE Kids"),
    SPHERE_FLEX("SPHERE X"),
    SOUND_FLEX("SOUND X"),
    PREMIUM("PREMIUM");
    private String value;

    @Override
    public String getCode() {
        return name();
    }
}