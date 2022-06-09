package study.movie.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.domain.payment.service.PaymentService;
import study.movie.domain.ticket.dto.request.PaymentRequest;
import study.movie.global.dto.CustomResponse;

import static study.movie.global.constants.ResponseMessage.VERIFY_PAYMENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentApiController {
    private final PaymentService paymentService;

    @PostMapping("/complete/{imp_uid}")
    public ResponseEntity<?> completePayment(@PathVariable("imp_uid") String impUid, @RequestBody PaymentRequest request) {
        paymentService.verifyPayment(impUid, request);
        return CustomResponse.success(VERIFY_PAYMENT);
    }
}
