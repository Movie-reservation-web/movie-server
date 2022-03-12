package study.movie.domain.movie;

import lombok.RequiredArgsConstructor;
import study.movie.model.EnumModel;

@RequiredArgsConstructor
public enum MovieType implements EnumModel {
    TWO_DIMENSIONS("2D"),
    THREE_DIMENSIONS("3D"),
    FOUR_DIMENSIONS_FLEX("4DX"),
    IMAX("IMAX"),
    SCREEN_X("SCREEN-X");

    private final String value;

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }
}
