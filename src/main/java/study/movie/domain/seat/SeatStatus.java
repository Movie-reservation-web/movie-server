package study.movie.domain.seat;

import lombok.RequiredArgsConstructor;
import study.movie.tools.enummodel.EnumModel;

@RequiredArgsConstructor
public enum SeatStatus implements EnumModel {
    EMPTY("빈 좌석"),
    RESERVED("예매완료");

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