package study.movie.iamport;

import com.siot.IamportRestClient.exception.IamportResponseException;

import java.io.IOException;

public interface IamportService {

    boolean doPayment(String imp_uid) throws IamportResponseException, IOException;
    void cancelPayment(String imp_uid) throws IamportResponseException, IOException;
}
