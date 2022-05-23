package study.movie.domain.ticket.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import study.movie.domain.theater.entity.ScreenFormat;
import study.movie.domain.ticket.entity.payment.AgeType;

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
