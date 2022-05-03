package study.movie.repository.schedule;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.Seat;
import study.movie.domain.schedule.SeatEntity;
import study.movie.domain.theater.ScreenFormat;
import study.movie.dto.schedule.condition.ScheduleBasicSearchCond;
import study.movie.dto.schedule.condition.ScheduleSearchCond;
import study.movie.dto.schedule.condition.UpdateSeatRequest;
import study.movie.dto.schedule.request.ScheduleScreenRequest;
import study.movie.global.utils.BasicRepositoryUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;
import static org.springframework.util.StringUtils.hasText;
import static study.movie.domain.movie.QMovie.movie;
import static study.movie.domain.schedule.QSchedule.schedule;
import static study.movie.domain.schedule.QSeatEntity.seatEntity;
import static study.movie.domain.schedule.ScheduleStatus.CLOSED;
import static study.movie.domain.schedule.ScheduleStatus.OPEN;
import static study.movie.domain.theater.QScreen.screen;
import static study.movie.domain.theater.QTheater.theater;


@Repository
@RequiredArgsConstructor
@Slf4j
public class ScheduleRepositoryImpl extends BasicRepositoryUtils implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private JPAQuery<Schedule> getScheduleJPAQueryFetch() {
        return queryFactory.selectFrom(schedule)
                .join(schedule.movie, movie).fetchJoin()
                .join(schedule.screen, screen).fetchJoin()
                .join(screen.theater, theater).fetchJoin();
    }

    @Override
    public List<Schedule> findAllSchedules() {
        return getScheduleJPAQueryFetch().fetch();
    }

    @Override
    public List<Schedule> searchBasicSchedules(ScheduleBasicSearchCond cond) {
        return getScheduleJPAQueryFetch()
                .where(
                        dateTimeAfter(schedule.screenTime.startDateTime, LocalDateTime.now()),
                        movieTitleEq(cond.getMovieTitle()),
                        screenFormatEq(screen.format, cond.getScreenFormat()),
                        theaterNameEq(cond.getTheaterName()),
                        reserveDateBetween(cond.getScreenDate())
                )
                .fetch();
    }

    @Override
    public List<Schedule> searchScheduleScreens(ScheduleScreenRequest request) {
        return queryFactory.selectFrom(schedule)
                .join(schedule.screen, screen).fetchJoin()
                .where(
                        dateTimeAfter(schedule.screenTime.startDateTime, LocalDateTime.now()),
                        movieTitleEq(request.getMovieTitle()),
                        screenFormatEq(screen.format, request.getScreenFormat()),
                        theaterNameEq(request.getTheaterName()),
                        reserveDateBetween(request.getScreenDate())
                )
                .fetch();
    }


    @Override
    public void updateSeatStatus(UpdateSeatRequest cond) {
        queryFactory.update(seatEntity)
                .set(seatEntity.status, cond.getStatus())
                .where(
                        seatsIn(cond.getSeats()),
                        scheduleAtSeatEntityEq(cond.getScheduleId()))
                .execute();
    }

    @Override
    public List<SeatEntity> findSeatByScheduleId(Long scheduleId) {
        return queryFactory.selectFrom(seatEntity)
                .join(seatEntity.schedule, schedule).fetchJoin()
                .where(schedule.id.eq(scheduleId))
                .fetch();
    }


    @Override
    public List<ScreenFormat> findFormatByMovie(String movieTitle) {
        return queryFactory.select(screen.format)
                .from(schedule)
                .join(schedule.movie, movie).fetchJoin()
                .join(schedule.screen, screen).fetchJoin()
                .where(
                        movieTitleEq(movieTitle),
                        scheduleStatusEq(OPEN)
                )
                .distinct()
                .fetch();
    }

    @Override
    public Page<Schedule> search(ScheduleSearchCond cond, Pageable pageable) {
        List<Schedule> content = getScheduleJPAQueryPaging(cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(scheduleSort(pageable))
                .fetch();

        JPAQuery<Long> countQuery = getScheduleJPAQueryCount(cond);

        return PageableExecutionUtils.getPage(
                content,
                pageable,
                countQuery::fetchOne);
    }

    private JPAQuery<Schedule> getScheduleJPAQueryPaging(ScheduleSearchCond cond) {
        return queryFactory.selectFrom(schedule)
                .join(schedule.movie, movie).fetchJoin()
                .join(schedule.screen, screen).fetchJoin()
                .join(screen.theater, theater).fetchJoin()
                .where(getSearchPredicts(cond));
    }

    private JPAQuery<Long> getScheduleJPAQueryCount(ScheduleSearchCond cond) {
        return queryFactory.select(schedule.count())
                .from(schedule)
                .leftJoin(schedule.movie, movie)
                .leftJoin(schedule.screen, screen)
                .leftJoin(screen.theater, theater)
                .where(getSearchPredicts(cond));
    }

    private Predicate[] getSearchPredicts(ScheduleSearchCond cond) {
        return new Predicate[]{
                movieTitleEq(cond.getMovieTitle()),
                theaterNameEq(cond.getTheaterName()),
                reserveDateBetween(cond.getScreenDate()),
                screenFormatEq(screen.format, cond.getScreenFormat()),
                scheduleStatusEq(cond.getScheduleStatus()),
                scheduleNumberEq(cond.getScheduleNumber())};
    }

    private BooleanExpression scheduleNumberEq(String scheduleNumber) {
        return hasText(scheduleNumber) ? schedule.scheduleNumber.eq(scheduleNumber) : null;
    }

    @Override
    public void updateStatusByPastDaysStatus(LocalDateTime dateTime) {
        queryFactory.update(schedule)
                .set(schedule.status, CLOSED)
                .where(
                        dateTimeBefore(schedule.screenTime.startDateTime, dateTime),
                        scheduleStatusNotEq(CLOSED)
                );
    }

    private BooleanExpression scheduleAtSeatEntityEq(Long scheduleId) {
        return scheduleId != null ? seatEntity.schedule.id.eq(scheduleId) : null;
    }

    private BooleanExpression seatsIn(List<Seat> seats) {
        return seats != null ? seatEntity.seat.in(seats) : null;
    }

    private BooleanExpression reserveDateBetween(LocalDate date) {
        return date != null ? schedule.screenTime.startDateTime.between(
                LocalDateTime.of(date, LocalTime.now().withMinute(0).withSecond(0).withNano(0)),
                LocalDateTime.of(date, LocalTime.MAX).withNano(0)
        ) : null;
    }

    private OrderSpecifier<?>[] scheduleSort(Pageable pageable) {
        return pageable.getSort().stream().map(order -> new OrderSpecifier(
                order.getDirection().isAscending() ? ASC : DESC,
                Expressions.path(Schedule.class, schedule, order.getProperty()))
        ).toArray(OrderSpecifier[]::new);
    }

}
