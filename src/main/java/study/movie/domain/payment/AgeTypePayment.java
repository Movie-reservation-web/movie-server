package study.movie.domain.payment;

import lombok.Getter;

import java.util.Map;

@Getter
public class AgeTypePayment {

    private final AgeType ageType;
    private final int price;
    private final int count;

    private AgeTypePayment(AgeType ageType, int price, int count) {
        this.ageType = ageType;
        this.price = price;
        this.count = count;
    }

    public static AgeTypePayment of(AgeType ageType, Map<AgeType, Integer> ageCriterionMap, int count) {
        return new AgeTypePayment(ageType, ageCriterionMap.get(ageType), count);
    }

    public int calcPayment(boolean fixRate, DateTimeType dateTimeType, DayWeekType dayWeekType) {
        return count * (!fixRate ? calcPolicy(price, dateTimeType, dayWeekType) : price);
    }

    private int calcPolicy(int price, DateTimeType dateTimeType, DayWeekType dayWeekType) {
        return dateTimeType.calcPolicy(dayWeekType.calcPolicy(price));
    }

    @Override
    public String toString() {
        return "Type:" + ageType +
                ", price:" + price;
    }
}
