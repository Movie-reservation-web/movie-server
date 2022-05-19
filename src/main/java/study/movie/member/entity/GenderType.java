package study.movie.member.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GenderType implements EnumMapperType {
    MALE("남성"),
    FEMALE("여성");
    private String value;

    @Override
    public String getCode() {
        return name();
    }
}