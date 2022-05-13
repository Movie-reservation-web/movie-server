package study.movie.ticket.entity.payment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AgeTypePayment {

    private final AgeType ageType;
    private final int price;
    private final int count;

    private AgeTypePayment(AgeType ageType, int price, int count) {
        this.ageType = ageType;
        this.price = price;
        this.count = count;
    }

    @Override
    public String toString() {
        return "Type:" + ageType +
                ", price:" + price;
    }
}
