package study.movie.ticket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import study.movie.ticket.service.PaymentServiceImpl;

@RestController
@RequiredArgsConstructor
public class PaymentController{

    private PaymentServiceImpl paymentService;

}
