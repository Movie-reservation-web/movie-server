package study.movie.domain.ticket.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel(description = "결제 정보 모델")
@Data
public class PaymentRequest {

    @Schema(description = "결제 금액", required = true)
    @NotNull
    @JsonProperty("paid_amount")
    private Number paidAmount;

    @Schema(description = "상품 아이디", required = true)
    @NotNull
    @JsonProperty("merchant_uid")
    private String merchantUid;
}
