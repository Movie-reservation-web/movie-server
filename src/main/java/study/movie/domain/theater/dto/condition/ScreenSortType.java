package study.movie.domain.theater.dto.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.metaModel.MetaModelType;

import static study.movie.domain.theater.entity.QScreen.screen;
import static study.movie.global.metaModel.MetaModelUtil.getColumn;

@Getter
@AllArgsConstructor
public enum ScreenSortType implements MetaModelType {
    SEAT_COUNT_ASC("좌석수 적은 순", "seat,asc", getColumn(screen.totalSeatCount)),
    SEAT_COUNT_DESC("좌석수 많은 순", "seat,desc", getColumn(screen.totalSeatCount));
    private String value;
    private String title;
    private String metaData;

    @Override
    public String getCode() {
        return title;
    }
}
