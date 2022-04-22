package study.movie.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayType {
    ACCOUNT_TRANSFER("계좌이체"),
    REMITTANCE("무통장입금"),
    CREDIT_CARD("신용카드"),
    CHECK_CARD("체크카드"),
    KAKAO_PAY("카카오페이"),
    NAVER_PAY("네이버페이"),
    TOSS("토스"),
    POINT("포인트"),
    MOBILE("휴대폰결제"),
    COUPON("쿠폰");
    private String title;
}
