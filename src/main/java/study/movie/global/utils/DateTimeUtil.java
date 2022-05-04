package study.movie.global.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeUtil {
    public static LocalDateTime dailyStartDateTime(LocalDate start) {
        return start != null ? LocalDateTime.of(start, LocalTime.MIDNIGHT) : null;
    }

    public static LocalDateTime dailyEndDateTime(LocalDate end) {
        return end != null ? LocalDateTime.of(end.plusDays(1), LocalTime.MIDNIGHT) : null;
    }

    public static LocalDateTime yearlyStartDateTime(Integer year) {
        return year != null ? dailyStartDateTime(LocalDate.ofYearDay(year, LocalDate.MIN.getDayOfYear())) : null;
    }

    public static LocalDateTime yearlyEndDateTime(Integer year) {
        return year != null ? dailyEndDateTime(LocalDate.ofYearDay(year, LocalDate.MAX.getDayOfYear())) : null;
    }


}
