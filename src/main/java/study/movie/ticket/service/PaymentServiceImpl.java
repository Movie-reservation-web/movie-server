package study.movie.ticket.service;

import com.siot.IamportRestClient.IamportClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import study.movie.ticket.entity.payment.AgeType;
import study.movie.ticket.entity.payment.DateTimeType;
import study.movie.ticket.entity.payment.DayWeekType;
import study.movie.theater.entity.ScreenFormat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Getter
    private final IamportClient client;

    public PaymentServiceImpl(@Value("${iamport.restkey}") String key, @Value("${iamport.secret}") String secret) {
        client = new IamportClient(key, secret);
    }

    @Override
    public Map<AgeType, Integer> getPriceMap(ScreenFormat format, LocalDateTime dateTime) {
        Map<AgeType, Integer> result = new HashMap<>(format.getAgeCriterionMap());
        for (AgeType type : AgeType.values()) {
            result.compute(type, (ageType, price) -> applyAllPolicies(price, format, dateTime));
        }
        return result;
    }

    private int applyAllPolicies(int price, ScreenFormat format, LocalDateTime dateTime) {
        return applyRatePolicy(
                price,
                format.isFixRate(),
                getDateTimeType(dateTime),
                getDayWeekType(dateTime));
    }

    private int applyRatePolicy(int price, boolean fixRate, DateTimeType dateTimeType, DayWeekType dayWeekType) {
        return !fixRate ? applyDateTimePolicy(price, dateTimeType, dayWeekType) : price;
    }

    private int applyDateTimePolicy(int price, DateTimeType dateTimeType, DayWeekType dayWeekType) {
        return dateTimeType.calcPolicy(dayWeekType.calcPolicy(price));
    }

    private DateTimeType getDateTimeType(LocalDateTime reservedTime) {
        return DateTimeType.findByCriterion(reservedTime.toLocalTime());
    }

    private DayWeekType getDayWeekType(LocalDateTime reservedTime) {
        return DayWeekType.findByCriterion(reservedTime.getDayOfWeek());
    }


}
