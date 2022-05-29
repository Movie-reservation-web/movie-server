package study.movie.domain.theater.dto.condition;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TheaterSearchType implements EnumMapperType {
    NAME("극장 이름"),
    CITY_CODE("도시 이름"),
    PHONE("전화번호");
    private String value;

    @Override
    public String getCode() {
        return name();
    }
}
