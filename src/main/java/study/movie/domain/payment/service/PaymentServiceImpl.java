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
        if (request.getPaidAmount().intValue() != impPayment.getAmount().intValue()) {
            throw new CustomException(DIFFERENT_AMOUNT);
        }
        if (!request.getMerchantUid().equals(impPayment.getMerchantUid())) {
            throw new CustomException(DIFFERENT_MERCHANT);
        }
        Payment payment = Payment.builder().impPayment(impPayment).build();
        paymentRepository.save(payment);
    }
}
