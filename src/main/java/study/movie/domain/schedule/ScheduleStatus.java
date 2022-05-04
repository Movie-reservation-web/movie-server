package study.movie.domain.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

import java.util.function.Function;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ScheduleStatus implements EnumMapperType {
    OPEN("예매가능", count -> count + "석"),
    CLOSED("예매종료", count -> "예매종료"),
    SOLD_OUT("매진", count -> "매진");

    private String value;
    @JsonIgnore
    private Function<Integer, String> convert;

    public String convertSeatCount(int seatCount) {
        return convert.apply(seatCount);
    }

    @Override
    public String getCode() {
        return name();
    }

}
