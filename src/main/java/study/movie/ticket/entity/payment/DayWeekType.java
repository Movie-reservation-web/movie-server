package study.movie.ticket.entity.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Function;

import static java.time.DayOfWeek.*;

@Getter
@AllArgsConstructor
public enum DayWeekType {
    WEEKDAY(price -> price, Set.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY)),
    WEEKEND(price -> price + 1000, Set.of(FRIDAY, SATURDAY, SUNDAY));
    private final Function<Integer, Integer> policy;
    private final Set<DayOfWeek> criterion;

    public static DayWeekType findByCriterion(DayOfWeek dayOfWeek) {
        return EnumSet.allOf(DayWeekType.class).stream()
                .filter(dayWeekType -> dayWeekType.hasDayOfWeek(dayOfWeek))
                .findAny()
                .orElseThrow(() -> new DateTimeException("올바르지 않은 날짜 형식입니다."));
    }

    public int calcPolicy(int price) {
        return policy.apply(price);
    }

    private boolean hasDayOfWeek(DayOfWeek dow) {
        return criterion.contains(dow);
    }
}