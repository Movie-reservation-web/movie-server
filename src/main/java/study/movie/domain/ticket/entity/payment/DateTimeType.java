package study.movie.domain.ticket.entity.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public enum DateTimeType {
    MORNING(price -> price - 4000, LocalTime.of(6, 0), LocalTime.of(10, 0)),
    BRUNCH(price -> price - 1000, LocalTime.of(10, 1), LocalTime.of(13, 0)),
    GENERAL(price -> price, LocalTime.of(13, 1), LocalTime.of(5, 59));
    private final Function<Integer, Integer> policy;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public int calcPolicy(int price) {
        return policy.apply(price);
    }

    public static DateTimeType findByCriterion(LocalTime time) {
        return EnumSet.allOf(DateTimeType.class).stream()
                .filter(type -> type.isBetweenTimeRange(time))
                .findAny()
                .orElseThrow(() -> new DateTimeException("올바르지 않은 날짜 형식입니다."));
    }

    private boolean isBetweenTimeRange(LocalTime target) {
        if (startTime.equals(target)) return true;
        return calcDayOff(!target.isBefore(startTime) && target.isBefore(endTime));
    }

    private boolean calcDayOff(boolean hasDayOfWeek) {
        return startTime.isAfter(endTime) != hasDayOfWeek;
    }
}
