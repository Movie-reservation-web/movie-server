package study.movie.service.ticket;

import study.movie.domain.payment.AgeType;
import study.movie.domain.theater.ScreenFormat;

import java.time.LocalDateTime;
import java.util.Map;

public interface PaymentService {

    Map<AgeType, Integer> getPriceMap(ScreenFormat format, LocalDateTime dateTime);
}
