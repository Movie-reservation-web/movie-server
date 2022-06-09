package study.movie.domain.payment.service;

import study.movie.domain.ticket.dto.request.PaymentRequest;

public interface PaymentService {

    void verifyPayment(String impUid, PaymentRequest request);
}
