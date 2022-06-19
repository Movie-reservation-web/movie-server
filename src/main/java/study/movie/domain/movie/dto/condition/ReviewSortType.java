package study.movie.domain.movie.dto.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.metaModel.MetaModelType;

import static study.movie.global.metaModel.MetaModelUtil.getColumn;
import static study.movie.domain.movie.entity.QReview.review;

@Getter
@AllArgsConstructor
public enum ReviewSortType implements MetaModelType {
    ID_DESC("최신 순", "id,desc", getColumn(review.id)),
    SCORE_ASC("평점 낮은순", "score,asc", getColumn(review.score)),
    SCORE_DESC("평점 높은순", "score,desc", getColumn(review.score));
    private String value;
    private String title;
    private String metaData;

    @Override
    public String getCode() {
        return title;
    }
}
