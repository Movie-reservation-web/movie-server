package study.movie.domain.payment;

import org.springframework.stereotype.Component;
import study.movie.domain.theater.ScreenFormat;

import java.time.LocalDateTime;


@Component
public class PaymentServiceUtil {

    public int calcPaymentAmount(AgeType ageType, ScreenFormat format, LocalDateTime reservedTime, int count) {
        return AgeTypePayment
                .of(ageType, format.getAgeCriterionMap(),count)
                .calcPayment(format.isFixRate(), getDateTimeType(reservedTime), getDayWeekType(reservedTime));
    }

    private DateTimeType getDateTimeType(LocalDateTime reservedTime) {
        return DateTimeType.findByCriterion(reservedTime.toLocalTime());
    }

    private DayWeekType getDayWeekType(LocalDateTime reservedTime) {
        return DayWeekType.findByCriterion(reservedTime.getDayOfWeek());
    }
}
