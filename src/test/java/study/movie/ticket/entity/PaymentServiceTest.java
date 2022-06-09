package study.movie.ticket.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import study.movie.domain.theater.entity.ScreenFormat;
import study.movie.domain.payment.entity.AgeType;
import study.movie.domain.payment.entity.DateTimeType;
import study.movie.domain.payment.entity.DayWeekType;
import study.movie.global.utils.PaymentUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest
public class PaymentServiceTest {

    @Test
    void 요일_타입_가져오기() {
        // given
        LocalDate weekDay = LocalDate.of(2022, 4, 21);
        LocalDate weekend = LocalDate.of(2022, 4, 24);

        // when
        DayWeekType type1 = DayWeekType.findByCriterion(weekDay.getDayOfWeek());
        DayWeekType type2 = DayWeekType.findByCriterion(weekend.getDayOfWeek());

        // then
        assertThat(type1).isEqualTo(DayWeekType.WEEKDAY);
        assertThat(type2).isEqualTo(DayWeekType.WEEKEND);
    }

    @Test
    void 시간대_타입_가져오기() {
        // given
        LocalTime morningTime = LocalTime.parse("07:00:00");
        LocalTime brunchTime = LocalTime.parse("11:25:00");
        LocalTime generalTime1 = LocalTime.parse("16:25:02");
        LocalTime generalTime2 = LocalTime.parse("01:40:21");

        // when
        DateTimeType type1 = DateTimeType.findByCriterion(morningTime);
        DateTimeType type2 = DateTimeType.findByCriterion(brunchTime);
        DateTimeType type3 = DateTimeType.findByCriterion(generalTime1);
        DateTimeType type4 = DateTimeType.findByCriterion(generalTime2);

        // then
        assertThat(type1).isEqualTo(DateTimeType.MORNING);
        assertThat(type2).isEqualTo(DateTimeType.BRUNCH);
        assertThat(type3).isEqualTo(DateTimeType.GENERAL);
        assertThat(type4).isEqualTo(DateTimeType.GENERAL);
    }

    @Test
    void 티켓_가격_정보_가져오기() {
        // given
        ScreenFormat format = ScreenFormat.TWO_D;
        LocalDateTime screenTime = LocalDateTime.of(2022, 4, 21, 16, 0, 0);

        // when
        Integer basicPrice = format.getAgeCriterionMap().get(AgeType.ADULT);
        DateTimeType dateTimeType = DateTimeType.findByCriterion(screenTime.toLocalTime());
        DayWeekType dayWeekType = DayWeekType.findByCriterion(screenTime.getDayOfWeek());
        int applyTimePrice = dateTimeType.calcPolicy(dayWeekType.calcPolicy(basicPrice));
        int finalPrice = format.isFixRate() ? applyTimePrice : basicPrice;

        Map<AgeType, Integer> priceMap = PaymentUtil.getPriceMap(format, screenTime);
        // then
        assertThat(priceMap.get(AgeType.ADULT)).isEqualTo(finalPrice);
    }
}
