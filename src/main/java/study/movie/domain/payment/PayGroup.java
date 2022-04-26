package study.movie.domain.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PayGroup implements EnumMapperType {
    CASH("현금", Arrays.asList(PayType.ACCOUNT_TRANSFER, PayType.REMITTANCE, PayType.TOSS)),
    CARD("카드", Arrays.asList(PayType.CREDIT_CARD, PayType.CHECK_CARD, PayType.KAKAO_PAY, PayType.NAVER_PAY)),
    ETC("기타", Arrays.asList(PayType.POINT, PayType.COUPON, PayType.MOBILE)),
    EMPTY("없음", Collections.EMPTY_LIST);
    private String value;

    private List<PayType> payTypes;

    public static PayGroup findByPayType(PayType payType) {
        return EnumSet.allOf(PayGroup.class).stream()
                .filter(payGroup -> payGroup.hasPayCode(payType))
                .findAny()
                .orElse(EMPTY);
    }

    private boolean hasPayCode(PayType payType) {
        return payTypes.stream()
                .anyMatch(type -> type == payType);
    }

    @Override
    public String getCode() {
        return name();
    }
}
