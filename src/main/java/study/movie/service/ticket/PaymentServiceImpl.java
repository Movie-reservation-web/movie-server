package study.movie.service.ticket;

import com.siot.IamportRestClient.IamportClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{
    @Getter
    private final IamportClient client;

    public PaymentServiceImpl(@Value("${iamport.restkey}") String key, @Value("${iamport.secret}") String secret) {
        client = new IamportClient(key, secret);
    }


}
