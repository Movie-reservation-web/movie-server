package study.movie.iamport;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IamportServiceImple implements IamportService {

    private IamportClient iamportClient;
    private String impKey = "1222340615843470";
    private String impSecret = "6be681f729827941d171401f45187414fd4f92284824201abac3be1bcf478761c5626295464f91cb";

    @Override
    public boolean doPayment(String imp_uid) throws IamportResponseException, IOException {
        //iamportClient 생성
        this.iamportClient = new IamportClient(impKey, impSecret);

        //결제 정보 조회하기
        IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(imp_uid);
        Payment paymentData = paymentResponse.getResponse();
        String merchantUid = paymentData.getMerchantUid();
        double amount = paymentData.getAmount().doubleValue();
        String status = paymentData.getStatus();

        //db에서 결제되야할 상품 가격 가져와서 response.amount와 가격 비교
        //int amountToBePaid = Orders.findById(paymentData.merchant_uid);
        double amountToBePaid = 100; //test

        return amountToBePaid == amount;
    }

    @Override
    public void cancelPayment(String imp_uid) throws IamportResponseException, IOException {
        CancelData cancelData = new CancelData(imp_uid, true);
        iamportClient.cancelPaymentByImpUid(cancelData);
    }
}
