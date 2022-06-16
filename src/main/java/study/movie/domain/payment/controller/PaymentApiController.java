package study.movie.domain.payment.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.domain.payment.service.PaymentService;
import study.movie.domain.ticket.dto.request.PaymentRequest;
import study.movie.global.dto.CustomResponse;

import static study.movie.global.constants.ResponseMessage.VERIFY_PAYMENT;

@Api(value = "Payment Api Controller", tags = "[Api] Payment")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentApiController {
    private final PaymentService paymentService;

    @Operation(summary = "결제 검증", description = "결제 위변조 여부를 검사한다.")
    @Parameters({@Parameter(name = "id", description = "주문번호의 id", required = true, in = ParameterIn.PATH)})
    @PostMapping("/complete/{imp_uid}")
    public ResponseEntity<?> completePayment(
            @PathVariable("imp_uid") String impUid, @RequestBody PaymentRequest request) {
        paymentService.verifyPayment(impUid, request);
        return CustomResponse.success(VERIFY_PAYMENT);
    }
}
