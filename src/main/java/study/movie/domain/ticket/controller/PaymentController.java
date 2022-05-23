package study.movie.domain.ticket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import study.movie.domain.ticket.service.PaymentServiceImpl;

@RestController
@RequiredArgsConstructor
public class PaymentController{

    private PaymentServiceImpl paymentService;

}
