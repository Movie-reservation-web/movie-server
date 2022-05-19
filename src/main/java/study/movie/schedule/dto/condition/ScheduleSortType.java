package study.movie.schedule.dto.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.metaModel.MetaModelType;

import static study.movie.global.metaModel.MetaModelUtil.getColumn;
import static study.movie.movie.entity.QMovie.movie;

@Getter
@AllArgsConstructor
public enum ScheduleSortType implements MetaModelType {
    ID_ASC("오래된 순", "id,asc", getColumn(movie.id)),
    ID_DESC("최신 순", "id,desc", getColumn(movie.id)),
    AUDIENCE_ASC("적은 관객 순", "audience,asc", getColumn(movie.audience)),
    AUDIENCE_DESC("많은 관객 순", "audience,desc", getColumn(movie.audience));
    private String value;
    private String title;
    private String metaData;

    @Override
    public String getCode() {
        return title;
    }
}