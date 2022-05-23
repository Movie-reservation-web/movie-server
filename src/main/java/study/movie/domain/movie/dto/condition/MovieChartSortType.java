package study.movie.domain.movie.dto.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.metaModel.MetaModelType;

import static study.movie.global.metaModel.MetaModelUtil.getColumn;
import static study.movie.domain.movie.entity.QMovie.movie;

@Getter
@AllArgsConstructor
public enum MovieChartSortType implements MetaModelType {
    AUDIENCE_DESC("관람객순", "audience,desc", getColumn(movie.audience)),
    SCORE_DESC("평점순", "avg-score,desc", getColumn(movie.avgScore));
    private String value;
    private String title;
    private String metaData;

    @Override
    public String getCode() {
        return title;
    }
}
