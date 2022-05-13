package study.movie.ticket.service;

import study.movie.ticket.entity.payment.AgeType;
import study.movie.theater.entity.ScreenFormat;

import java.time.LocalDateTime;
import java.util.Map;

public interface PaymentService {

    Map<AgeType, Integer> getPriceMap(ScreenFormat format, LocalDateTime dateTime);
}
