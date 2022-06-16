package study.movie.domain.movie.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FilmRating implements EnumMapperType {
    UNDETERMINED("미정"),
    G_RATED("전체"),
    PG_12("12세 이상"),
    PG_15("15세 이상"),
    R_RATED("청소년 관람불가");

    private String value;

    @Override
    public String getCode() {
        return name();
    }
}
