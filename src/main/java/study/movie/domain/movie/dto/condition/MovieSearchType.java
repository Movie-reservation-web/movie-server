package study.movie.domain.movie.dto.condition;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MovieSearchType implements EnumMapperType {
    TITLE("제목"),
    DIRECTOR("감독 이름"),
    ACTOR("배우 이름"),
    FILM_RATING("등급"),
    FILM_FORMAT("영화 타입"),
    NATION("국가"),
    GENRE("영화 장르");

    private String value;

    @Override
    public String getCode() {
        return name();
    }
}
