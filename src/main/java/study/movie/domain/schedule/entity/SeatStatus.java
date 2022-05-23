package study.movie.domain.schedule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SeatStatus implements EnumMapperType {
    RESERVED("예매완료"), NOT_ALLOW("선택불가"), EMPTY("선택가능"), SELECTED("선택");
    private String value;

    @Override
    public String getCode() {
        return name();
    }
}
