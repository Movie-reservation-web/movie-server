package study.movie.domain.ticket.service;

import study.movie.domain.theater.entity.ScreenFormat;
import study.movie.domain.ticket.entity.payment.AgeType;

import java.time.LocalDateTime;
import java.util.Map;

public interface PaymentService {

    Map<AgeType, Integer> getPriceMap(ScreenFormat format, LocalDateTime dateTime);
}
