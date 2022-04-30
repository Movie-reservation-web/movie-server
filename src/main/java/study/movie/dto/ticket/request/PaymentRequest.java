package study.movie.dto.ticket.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservedTime;
}
