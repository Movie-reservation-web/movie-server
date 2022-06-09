package study.movie;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IamportTest {

    IamportClient client;

    @Test
    public void testGetToken() {
        String test_api_key = "0349213312895772";
        String test_api_secret = "33cc95c0a912987164e4c8dbef320288f8da0843b93daf2cede64b6d237b9b28d0360c8b3d60108c";
        client = new IamportClient(test_api_key, test_api_secret);
        try {
            IamportResponse<AccessToken> auth_response = client.getAuth();

            assertNotNull(auth_response.getResponse());
            assertNotNull(auth_response.getResponse().getToken());
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            //서버 연결 실패
            e.printStackTrace();
        }
    }
}
