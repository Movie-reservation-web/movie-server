package study.movie.domain.movie;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FilmRating implements EnumMapperType {
    UNDETERMINED("미정"),
    G_RATED("전체관람가"),
    PG_12("12세 이상 관람가"),
    PG_15("15세 이상 관람가"),
    X_RATED("18세 이상 관람가"),
    R_RATED("청소년 관람불가");

    private String value;

    @Override
    public String getCode() {
        return name();
    }
}