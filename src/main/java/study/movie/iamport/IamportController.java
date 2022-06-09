package study.movie.iamport;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.global.dto.CustomResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static study.movie.global.constants.ResponseMessage.*;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/admin/v1/iamport")
public class IamportController {

    private final IamportService iamportService;

    //프론트에서 결제 완료 시 엔드포인트, 결제 정보 검증 및 저장
    @ResponseBody
    @RequestMapping(value="/payment/complete")
    public ResponseEntity<?> paymentByImpUid(@RequestBody Map<String, String> req) throws IamportResponseException, IOException {
        //결제번호, 주문번호 추출하기
        String imp_uid = req.get("imp_uid");
        //String merchant_uid = req.get("merchant_uid");

        if(iamportService.doPayment(imp_uid)){
            //결제 성공
            //db 저장
            //Orders.findByIdAndUpdate(merchant_uid, { $set: paymentData });
            return CustomResponse.success(PAYMENT_SUCCESS);
        }else{
            //금액 위변조로 결제 취소
            iamportService.cancelPayment(imp_uid);
            return CustomResponse.success(PAYMENT_FAIL);
        }
    }
}