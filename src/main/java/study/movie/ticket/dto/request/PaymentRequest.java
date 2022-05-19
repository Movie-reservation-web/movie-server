package study.movie.ticket.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import study.movie.ticket.entity.payment.AgeType;
import study.movie.theater.entity.ScreenFormat;

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
