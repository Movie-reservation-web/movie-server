package study.movie.domain.ticket.entity.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AgeType implements EnumMapperType {
    ADULT("일반"),
    TEENAGER("청소년");

    private String value;

    @Override
    public String getCode() {
        return name();
    }
}
