package study.movie.domain.payment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PaymentStatus implements EnumMapperType {
    PAID("결제 완료"),
    CANCEL("결제 취소");
    private String value;

    @Override
    public String getCode() {
        return name();
    }

}
