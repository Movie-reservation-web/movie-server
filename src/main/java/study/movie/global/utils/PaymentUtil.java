package study.movie.global.utils;

import study.movie.domain.theater.entity.ScreenFormat;
import study.movie.domain.payment.entity.AgeType;
import study.movie.domain.payment.entity.DateTimeType;
import study.movie.domain.payment.entity.DayWeekType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public abstract class PaymentUtil {

    public static Map<AgeType, Integer> getPriceMap(ScreenFormat format, LocalDateTime dateTime) {
        Map<AgeType, Integer> result = new HashMap<>(format.getAgeCriterionMap());
        for (AgeType type : AgeType.values()) {
            result.compute(type, (ageType, price) -> applyAllPolicies(price, format, dateTime));
        }
        return result;
    }

    private static int applyAllPolicies(int price, ScreenFormat format, LocalDateTime dateTime) {
        return applyRatePolicy(
                price,
                format.isFixRate(),
                getDateTimeType(dateTime),
                getDayWeekType(dateTime));
    }

    private static int applyRatePolicy(int price, boolean fixRate, DateTimeType dateTimeType, DayWeekType dayWeekType) {
        return !fixRate ? applyDateTimePolicy(price, dateTimeType, dayWeekType) : price;
    }

    private static int applyDateTimePolicy(int price, DateTimeType dateTimeType, DayWeekType dayWeekType) {
        return dateTimeType.calcPolicy(dayWeekType.calcPolicy(price));
    }

    private static DateTimeType getDateTimeType(LocalDateTime reservedTime) {
        return DateTimeType.findByCriterion(reservedTime.toLocalTime());
    }

    private static DayWeekType getDayWeekType(LocalDateTime reservedTime) {
        return DayWeekType.findByCriterion(reservedTime.getDayOfWeek());
    }
}
