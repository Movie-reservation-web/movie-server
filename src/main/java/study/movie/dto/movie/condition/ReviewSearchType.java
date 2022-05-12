package study.movie.dto.movie.condition;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ReviewSearchType implements EnumMapperType {
    WRITER("작성자"),
    MOVIE_TITLE("영화 제목");

    private String value;

    @Override
    public String getCode() {
        return name();
    }
}