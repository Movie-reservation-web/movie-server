package study.movie.domain.theater.dto.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.metaModel.MetaModelType;

import static study.movie.domain.theater.entity.QTheater.theater;
import static study.movie.global.metaModel.MetaModelUtil.getColumn;

@Getter
@AllArgsConstructor
public enum TheaterSortType implements MetaModelType {
    NAME_ASC("이름 순(ㄱ-ㅎ)", "name,asc", getColumn(theater.name)),
    NAME_DESC("이름 순(ㅎ-ㄱ)", "name,desc", getColumn(theater.name)),
    CITY_ASC("이름 순(ㄱ-ㅎ)", "city,asc", getColumn(theater.city)),
    CITY_DESC("이름 순(ㅎ-ㄱ)", "city,desc", getColumn(theater.city));
    private String value;
    private String title;
    private String metaData;

    @Override
    public String getCode() {
        return title;
    }
}
