package study.movie.domain.payment.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.exception.CustomException;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;

import static study.movie.exception.ErrorCode.ALREADY_CANCELLED_PAYMENT;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    private String merchantUid;

    private Integer amount;

    private String buyerName;

    private String buyerMobile;

    private String buyerEmail;

    private PaymentStatus status;

    //==생성 메서드==//
    @Builder
    public Payment(com.siot.IamportRestClient.response.Payment impPayment) {
        this.merchantUid = impPayment.getMerchantUid();
        this.amount = impPayment.getAmount().intValue();
        this.buyerName = impPayment.getBuyerName();
        this.buyerMobile = impPayment.getBuyerTel();
        this.buyerEmail = impPayment.getBuyerEmail();
        this.status = PaymentStatus.PAID;
    }

    public void cancelPayment(){
        if (status != PaymentStatus.PAID) throw new CustomException(ALREADY_CANCELLED_PAYMENT);
        this.status = PaymentStatus.CANCEL;
    }
}
