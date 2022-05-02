package study.movie.global.utils;

import com.querydsl.core.types.dsl.*;
import study.movie.domain.schedule.ScheduleStatus;
import study.movie.domain.theater.ScreenFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.springframework.util.StringUtils.hasText;
import static study.movie.domain.member.QMember.member;
import static study.movie.domain.movie.QMovie.movie;
import static study.movie.domain.schedule.QSchedule.schedule;
import static study.movie.domain.theater.QTheater.theater;

public abstract class BasicRepositoryUtils {

    protected BooleanExpression dateBefore(DateTimePath<LocalDateTime> dateTimePath, LocalDate date) {
        return date != null ? dateTimePath.before(
                LocalDateTime.of(date, LocalTime.MAX.withNano(0))
        ) : null;
    }

    protected BooleanExpression dateAfter(DateTimePath<LocalDateTime> dateTimePath, LocalDate date) {
        return date != null ? dateTimePath.after(
                LocalDateTime.of(date, LocalTime.MIN.withNano(0))
        ) : null;
    }

    protected BooleanExpression dateTimeBefore(DateTimePath<LocalDateTime> dateTimePath, LocalDateTime dateTime) {
        return dateTime != null ? dateTimePath.before(dateTime) : null;
    }

    protected BooleanExpression dateTimeAfter(DateTimePath<LocalDateTime> dateTimePath, LocalDateTime dateTime) {
        return dateTime != null ? dateTimePath.after(dateTime) : null;
    }

    private BooleanExpression stringPathEq(StringPath path, String data) {
        return path.eq(data);
    }

    protected BooleanExpression movieTitleEq(String data) {
        return hasText(data) ? stringPathEq(movie.title, data) : null;
    }

    protected BooleanExpression theaterNameEq(String data) {
        return hasText(data) ? stringPathEq(theater.name, data) : null;
    }

    protected BooleanExpression memberNameEq(String data) {
        return hasText(data) ? stringPathEq(member.name, data) : null;
    }

    protected BooleanExpression scheduleStatusNotEq(ScheduleStatus status) {
        return status != null ? schedule.status.ne(status) : null;
    }

    protected BooleanExpression screenFormatEq(EnumPath<ScreenFormat> screenFormatPath, ScreenFormat format) {
        return format != null ? screenFormatPath.eq(format) : null;
    }

    protected BooleanExpression entityIdEq(NumberPath<Long> entityPath, Long id) {
        return id != null ? entityPath.eq(id) : null;
    }

    protected BooleanExpression entityIdLt(NumberPath<Long> entityPath, Long id) {
        return id != null ? entityPath.lt(id) : null;
    }

    protected BooleanExpression scheduleStatusEq(ScheduleStatus status) {
        return status != null ? schedule.status.eq(status) : null;
    }
}
