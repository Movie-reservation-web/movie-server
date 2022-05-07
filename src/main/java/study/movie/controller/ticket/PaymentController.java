package study.movie.controller.ticket;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.movie.service.ticket.PaymentServiceImpl;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PaymentController{

    private PaymentServiceImpl paymentService;

    @GetMapping("/verifyIamport/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String impUID) throws IamportResponseException, IOException {
        return paymentService.getClient().paymentByImpUid(impUID);

    }

}
