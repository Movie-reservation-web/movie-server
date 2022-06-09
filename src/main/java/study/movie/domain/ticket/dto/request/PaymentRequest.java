package study.movie.domain.ticket.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PaymentRequest {

    @NotNull
    @JsonProperty("paid_amount")
    private Number paidAmount;

    @NotNull
    @JsonProperty("merchant_uid")
    private String merchantUid;
}
