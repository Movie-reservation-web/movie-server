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
import study.movie.domain.schedule.ScheduleStatus;
import study.movie.domain.schedule.Seat;
import study.movie.domain.schedule.SeatEntity;
import study.movie.domain.theater.ScreenFormat;
import study.movie.dto.schedule.condition.ScheduleBasicSearchCond;
import study.movie.dto.schedule.condition.ScheduleSearchCond;
import study.movie.dto.schedule.condition.UpdateSeatRequest;
import study.movie.dto.schedule.request.ScheduleScreenRequest;
import study.movie.global.utils.BasicRepositoryUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;
import static study.movie.domain.movie.QMovie.movie;
import static study.movie.domain.schedule.QSchedule.schedule;
import static study.movie.domain.schedule.QSeatEntity.seatEntity;
import static study.movie.domain.schedule.ScheduleStatus.CLOSED;
import static study.movie.domain.schedule.ScheduleStatus.OPEN;
import static study.movie.domain.theater.QScreen.screen;
import static study.movie.domain.theater.QTheater.theater;
import static study.movie.global.utils.DateTimeUtil.dailyEndDateTime;
import static study.movie.global.utils.DateTimeUtil.dailyStartDateTime;


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
                        dateTimeGoe(LocalDateTime.now()),
                        dateTimeLt(dailyEndDateTime(cond.getScreenDate())),
                        movie.id.eq(cond.getMovieId()),
                        screen.format.eq(cond.getScreenFormat()),
                        theater.id.eq(cond.getTheaterId())
                )
                .fetch();
    }

    @Override
    public List<Schedule> searchScheduleScreens(ScheduleScreenRequest request) {
        return queryFactory.selectFrom(schedule)
                .join(schedule.screen, screen).fetchJoin()
                .where(
                        dateTimeGoe(LocalDateTime.now()),
                        dateTimeLt(dailyEndDateTime(request.getScreenDate())),
                        movie.id.eq(request.getMovieId()),
                        screen.format.eq(request.getScreenFormat()),
                        theater.id.eq(request.getTheaterId())
                )
                .fetch();
    }


    @Override
    public void updateSeatStatus(UpdateSeatRequest cond) {
        queryFactory.update(seatEntity)
                .set(seatEntity.status, cond.getStatus())
                .where(
                        seatsIn(cond.getSeats()),
                        scheduleIdEq(cond.getScheduleId()))
                .execute();
    }

    @Override
    public List<SeatEntity> findSeatByScheduleId(Long scheduleId) {
        return queryFactory.selectFrom(seatEntity)
                .join(seatEntity.schedule, schedule).fetchJoin()
                .where(scheduleIdEq(scheduleId))
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

    @Override
    public void updateStatusByPastDaysStatus(LocalDateTime dateTime) {
        queryFactory.update(schedule)
                .set(schedule.status, CLOSED)
                .where(
                        dateTimeLt(dateTime),
                        scheduleStatusNe(CLOSED)
                );
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
                dateTimeGoe(dailyStartDateTime(cond.getScreenStartDate())),
                dateTimeLt(dailyEndDateTime(cond.getScreenEndDate())),
                screenFormatEq(cond.getScreenFormat()),
                scheduleStatusEq(cond.getStatus())
        };
    }

    protected BooleanExpression scheduleStatusEq(ScheduleStatus status) {
        return status != null ? schedule.status.eq(status) : null;
    }

    protected BooleanExpression scheduleStatusNe(ScheduleStatus status) {
        return status != null ? schedule.status.ne(status) : null;
    }

    private BooleanExpression screenFormatEq(ScreenFormat format) {
        return format != null ? screen.format.eq(format) : null;
    }

    private BooleanExpression scheduleIdEq(Long scheduleId) {
        return scheduleId != null ? seatEntity.schedule.id.eq(scheduleId) : null;
    }

    private BooleanExpression isOpenSchedule(LocalDateTime endDateTime) {
        return dateTimeGoe(LocalDateTime.now())
                .and(dateTimeLt(endDateTime));
    }

    private BooleanExpression dateTimeLt(LocalDateTime dateTime) {
        return dateTime != null ? schedule.screenTime.startDateTime.lt(dateTime) : null;
    }

    private BooleanExpression dateTimeGoe(LocalDateTime dateTime) {
        return dateTime != null ? schedule.screenTime.startDateTime.goe(dateTime) : null;
    }

    private BooleanExpression seatsIn(List<Seat> seats) {
        return seats != null ? seatEntity.seat.in(seats) : null;
    }

    private OrderSpecifier<?>[] scheduleSort(Pageable pageable) {
        return pageable.getSort().stream().map(order -> new OrderSpecifier(
                order.getDirection().isAscending() ? ASC : DESC,
                Expressions.path(Schedule.class, schedule, order.getProperty()))
        ).toArray(OrderSpecifier[]::new);
    }

}
