package study.movie.dto.ticket.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import study.movie.domain.payment.AgeType;
import study.movie.domain.theater.ScreenFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class PaymentRequest {

    @NotNull
    private Map<AgeType, Integer> peopleInfo;

    @NotNull
    private ScreenFormat screenFormat;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime screenDateTime;
}
