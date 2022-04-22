package study.movie.dto.ticket;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class PaymentRequest {

    @Range(min = 0, max = 8)
    private int adultCount;
    @Range(min = 0, max = 8)
    private int teenagerCount;
    @NotNull
    private LocalDateTime reservedTime;
}
