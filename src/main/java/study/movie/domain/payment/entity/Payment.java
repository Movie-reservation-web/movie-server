package study.movie.domain.payment.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.exception.CustomException;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;

import static study.movie.exception.ErrorCode.ALREADY_CANCELLED_PAYMENT;

@ApiModel(description = "결제 모델")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {

    @Schema(description = "결제 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Schema(description = "주문번호", required = true)
    private String merchantUid;

    @Schema(description = "결제금액", required = true)
    private Integer amount;

    @Schema(description = "결제자", required = true)
   private String buyerName;

    @Schema(description = "결제자 전화번호", required = true)
    private String buyerMobile;

    @Schema(description = "결제자 이메일", required = true)
    private String buyerEmail;

    @Schema(description = "결제 상태", required = true)
    @Enumerated(EnumType.STRING)
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
