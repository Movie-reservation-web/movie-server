package study.movie.domain.ticket;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import study.movie.domain.payment.AgeType;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.payment.DateTimeType;
import study.movie.domain.payment.DayWeekType;
import study.movie.domain.payment.PaymentServiceUtil;
import study.movie.dto.ticket.PaymentRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static study.movie.domain.payment.AgeType.ADULT;
import static study.movie.domain.payment.AgeType.TEENAGER;


@Slf4j
public class PaymentServiceUtilTest {
    @Autowired
    private PaymentServiceUtil paymentServiceUtil;

    @Test
    public void 요일_타입_가져오기() throws Exception {
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
    public void 시간대_타입_가져오기() throws Exception {
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
    public void 티켓_금액_계산_변동_금액() throws Exception {
        // given
        PaymentRequest request1 = new PaymentRequest();
        request1.setTeenagerCount(1);
        request1.setAdultCount(2);
        request1.setReservedTime(LocalDateTime.of(2022, 4, 21, 16, 0, 0));
        ScreenFormat format1 = ScreenFormat.TWO_D;

        PaymentRequest request2 = new PaymentRequest();
        request2.setAdultCount(3);
        request2.setTeenagerCount(2);
        request2.setReservedTime(LocalDateTime.of(2022, 4, 24, 11, 0, 0));
        ScreenFormat format2 = ScreenFormat.IMAX;

        // when
        // 성인2,청소년1,평일,일반,2D 영화
        int totalPrice1 = (format1.getPrice(ADULT) * 2) + (format1.getPrice(AgeType.TEENAGER));

        int paymentAmount1 = paymentServiceUtil.calcPaymentAmount(ADULT, format1, request1.getReservedTime(), request1.getAdultCount()) +
                paymentServiceUtil.calcPaymentAmount(TEENAGER, format1, request1.getReservedTime(), request1.getTeenagerCount());


        // 성인3,청소년2,주말,브런치,IMAX
        int totalPrice2 = (format2.getPrice(ADULT) + 1000 - 1000) * 3 + (format2.getPrice(AgeType.TEENAGER) + 1000 - 1000) * 2;
        int paymentAmount2 = paymentServiceUtil.calcPaymentAmount(ADULT, format2, request2.getReservedTime(), request2.getAdultCount()) +
                paymentServiceUtil.calcPaymentAmount(TEENAGER, format2, request2.getReservedTime(), request2.getTeenagerCount());

        // then
        assertThat(paymentAmount1).isEqualTo(totalPrice1);
        assertThat(paymentAmount2).isEqualTo(totalPrice2);
    }

    @Test
    public void 티켓_금액_계산_고정_금액() throws Exception {
        // given
        PaymentRequest request1 = new PaymentRequest();
        request1.setAdultCount(2);
        request1.setTeenagerCount(1);
        request1.setReservedTime(LocalDateTime.of(2022, 4, 21, 16, 0, 0));

        PaymentRequest request2 = new PaymentRequest();
        request2.setAdultCount(3);
        request2.setTeenagerCount(2);
        request2.setReservedTime(LocalDateTime.of(2022, 4, 24, 11, 0, 0));

        ScreenFormat format = ScreenFormat.CINE_DE_CHEF;

        // when
        // 성인2,청소년1,평일,일반,Cine de chef
        int totalPrice1 = (format.getPrice(ADULT) * 2) + format.getPrice(TEENAGER);
        int paymentAmount1 = paymentServiceUtil.calcPaymentAmount(ADULT, format, request1.getReservedTime(), request1.getAdultCount()) +
                paymentServiceUtil.calcPaymentAmount(TEENAGER, format, request1.getReservedTime(), request1.getTeenagerCount());

        // 성인3,청소년2,주말,브런치,Cine de chef
        int totalPrice2 = (format.getPrice(ADULT) * 3) + (format.getPrice(TEENAGER) * 2);
        int paymentAmount2 = paymentServiceUtil.calcPaymentAmount(ADULT, format, request2.getReservedTime(), request2.getAdultCount()) +
                paymentServiceUtil.calcPaymentAmount(TEENAGER, format, request2.getReservedTime(), request2.getTeenagerCount());


        // then
        assertThat(paymentAmount1).isEqualTo(totalPrice1);
        assertThat(paymentAmount2).isEqualTo(totalPrice2);
    }
}
