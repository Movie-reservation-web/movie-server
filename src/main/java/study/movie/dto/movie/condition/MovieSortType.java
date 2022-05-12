package study.movie.dto.movie.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.metaModel.MetaModelType;

import static study.movie.domain.movie.QMovie.movie;
import static study.movie.global.metaModel.MetaModelUtil.getColumn;

@Getter
@AllArgsConstructor
public enum MovieSortType implements MetaModelType {
    ID_ASC("오래된순", "id,asc", getColumn(movie.id)),
    ID_DESC("최신순", "id,desc", getColumn(movie.id)),
    AUDIENCE_DESC("관람객순", "audience,desc", getColumn(movie.audience)),
    SCORE_DESC("평점순", "avg-score,desc", getColumn(movie.avgScore)),
    RUNNING_TIME_DESC("긴 상영시간순","running-time,desc",getColumn(movie.runningTime)),
    RUNNING_TIME_ASC("짧은 상영시간순","running-time,asc",getColumn(movie.runningTime));
    private String value;
    private String title;
    private String metaData;

    @Override
    public String getCode() {
        return title;
    }
}