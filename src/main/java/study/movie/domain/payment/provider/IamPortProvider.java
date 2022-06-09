package study.movie.domain.payment.provider;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.Payment;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import study.movie.global.config.IamportProperties;

@Component
public class IamPortProvider {

    private IamportProperties.Iamport iamport;
    private IamportClient client;

    public IamPortProvider(IamportProperties iamportProperties) {
        this.iamport = iamportProperties.getIamport();
        this.client = new IamportClient(iamport.getKey(), iamport.getSecret());
    }

    @SneakyThrows
    public Payment paymentByImpUid(String impUid) {
        return client.paymentByImpUid(impUid).getResponse();
    }

    @SneakyThrows
    public Payment cancelPayment(String merchantUid) {
        return client.cancelPaymentByImpUid(new CancelData(merchantUid, false)).getResponse();
    }
}
