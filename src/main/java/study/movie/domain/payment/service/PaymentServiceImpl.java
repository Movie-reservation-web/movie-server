package study.movie.domain.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.movie.domain.payment.entity.Payment;
import study.movie.domain.payment.provider.IamPortProvider;
import study.movie.domain.payment.repository.PaymentRepository;
import study.movie.domain.ticket.dto.request.PaymentRequest;
import study.movie.exception.CustomException;

import static study.movie.exception.ErrorCode.DIFFERENT_AMOUNT;
import static study.movie.exception.ErrorCode.DIFFERENT_MERCHANT;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final IamPortProvider iamPortProvider;

    private final PaymentRepository paymentRepository;

    @Override
    public void verifyPayment(String impUid, PaymentRequest request) {
        com.siot.IamportRestClient.response.Payment impPayment = iamPortProvider.paymentByImpUid(impUid);
        this.checkAmount(impPayment, request.getPaidAmount().intValue());
        this.checkMerchantUid(impPayment, request.getMerchantUid());
        paymentRepository.save(Payment.impBuilder().impPayment(impPayment).build());
    }

    private void checkAmount(com.siot.IamportRestClient.response.Payment impPayment, int requestAmount) {
        if (impPayment.getAmount().intValue() != requestAmount) {
            iamPortProvider.cancelPayment(impPayment.getMerchantUid());
            throw new CustomException(DIFFERENT_AMOUNT);
        }
    }

    private void checkMerchantUid(com.siot.IamportRestClient.response.Payment impPayment, String requestMerchantUid) {
        if (!impPayment.getMerchantUid().equals(requestMerchantUid)) {
            iamPortProvider.cancelPayment(impPayment.getMerchantUid());
            throw new CustomException(DIFFERENT_MERCHANT);
        }
    }
}
