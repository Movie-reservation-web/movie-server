package study.movie.repository.schedule;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import study.movie.domain.movie.Movie;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.ScheduleStatus;
import study.movie.domain.schedule.Seat;
import study.movie.domain.schedule.SeatEntity;
import study.movie.domain.theater.ScreenFormat;
import study.movie.dto.schedule.condition.ScheduleBasicSearchCond;
import study.movie.dto.schedule.condition.ScheduleSearchCond;
import study.movie.dto.schedule.condition.UpdateSeatCond;
import study.movie.dto.schedule.request.ScheduleScreenRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;
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
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private JPAQuery<Schedule> getScheduleJPAQueryFetch() {
        return queryFactory.selectFrom(schedule)
                .join(schedule.movie, movie).fetchJoin()
                .join(schedule.screen, screen).fetchJoin()
                .join(screen.theater, theater).fetchJoin();
    }

    private JPAQuery<Schedule> getScheduleJPAQueryPaging() {
        return queryFactory.selectFrom(schedule)
                .leftJoin(schedule.movie, movie)
                .leftJoin(schedule.screen, screen)
                .leftJoin(screen.theater, theater);
    }

    private JPAQuery<Long> getScheduleJPAQueryCount() {
        return queryFactory.select(schedule.count())
                .from(schedule)
                .leftJoin(schedule.movie, movie)
                .leftJoin(schedule.screen, screen)
                .leftJoin(screen.theater, theater);
    }

    @Override
    public List<Schedule> findAllSchedules() {
        return getScheduleJPAQueryFetch().fetch();
    }

    @Override
    public List<Schedule> searchBasicSchedules(ScheduleBasicSearchCond cond) {
        return getScheduleJPAQueryFetch()
                .where(
                        startDateTimeAfter(LocalDateTime.now()),
                        movieTitleEq(cond.getMovieTitle()),
                        screenFormatEq(cond.getFormat()),
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
                        startDateTimeAfter(LocalDateTime.now()),
                        movieTitleEq(request.getMovieTitle()),
                        screenFormatEq(request.getScreenFormat()),
                        theaterNameEq(request.getTheaterName()),
                        reserveDateBetween(request.getScreenDate())
                )
                .fetch();
    }

    @Override
    public void updateSeatStatus(UpdateSeatCond cond) {
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
    public Page<Schedule> search(ScheduleSearchCond cond, Pageable pageable, Integer totalElements) {
        List<Schedule> content = getScheduleJPAQueryPaging()
                .where(getSearchPredicts(cond))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(scheduleSort(pageable))
                .fetch();

        JPAQuery<Long> countQuery = getScheduleJPAQueryCount()
                .where(getSearchPredicts(cond));

        return PageableExecutionUtils.getPage(
                content,
                pageable,
                totalElements != null ? totalElements::longValue : countQuery::fetchOne);
    }

    private Predicate[] getSearchPredicts(ScheduleSearchCond cond) {
        return new Predicate[]{movieTitleEq(cond.getMovieTitle()),
                movieNationEq(cond.getMovieNation()),
                theaterNameEq(cond.getTheaterName()),
                reserveDateBetween(cond.getScreenDate()),
                screenFormatEq(cond.getScreenFormat()),
                scheduleStatusEq(cond.getScheduleStatus()),
                scheduleNumberEq(cond.getScheduleNumber())};
    }

    @Override
    public void updateStatusByPastDateTime(LocalDateTime dateTime) {
        queryFactory.update(schedule)
                .set(schedule.status, CLOSED)
                .where(
                        startDateTimeBefore(dateTime),
                        scheduleStatusNotEq(CLOSED)
                );
    }

    @Override
    public List<Movie> findMovieByOpenStatus() {
        return queryFactory.select(movie)
                .from(schedule)
                .join(schedule.movie, movie).fetchJoin()
                .where(
                        scheduleStatusEq(OPEN)
                )
                .groupBy(movie.id)
                .orderBy(movie.audience.desc())
                .fetch();
    }

    private BooleanExpression scheduleAtSeatEntityEq(Long scheduleId) {
        return scheduleId != null ? seatEntity.schedule.id.eq(scheduleId) : null;
    }

    private BooleanExpression scheduleStatusNotEq(ScheduleStatus status) {
        return status != null ? schedule.status.ne(status) : null;
    }

    private BooleanExpression scheduleStatusEq(ScheduleStatus status) {
        return status != null ? schedule.status.eq(status) : null;
    }

    private BooleanExpression scheduleNumberEq(String scheduleNumber) {
        return hasText(scheduleNumber) ? schedule.scheduleNumber.eq(scheduleNumber) : null;
    }

    private BooleanExpression seatsIn(List<Seat> seats) {
        return seats != null ? seatEntity.seat.in(seats) : null;
    }

    private BooleanExpression movieTitleEq(String movieTitle) {
        return hasText(movieTitle) ? movie.title.eq(movieTitle) : null;
    }

    private BooleanExpression movieNationEq(String nation) {
        return hasText(nation) ? movie.nation.eq(nation) : null;
    }

    private BooleanExpression screenFormatEq(ScreenFormat format) {
        return format != null ? screen.format.eq(format) : null;
    }

    private BooleanExpression theaterNameEq(String theaterName) {
        return hasText(theaterName) ? screen.theater.name.eq(theaterName) : null;
    }

    private BooleanExpression reserveDateBetween(LocalDate date) {
        return date != null ? schedule.screenTime.startDateTime.between(
                LocalDateTime.of(date, LocalTime.now().withMinute(0).withSecond(0).withNano(0)),
                LocalDateTime.of(date, LocalTime.MAX).withNano(0)
        ) : null;
    }

    private BooleanExpression startDateTimeBefore(LocalDateTime dateTime) {
        return dateTime != null ? schedule.screenTime.startDateTime.before(dateTime) : null;
    }

    private BooleanExpression startDateTimeAfter(LocalDateTime dateTime) {
        return dateTime != null ? schedule.screenTime.startDateTime.after(dateTime) : null;
    }

    private OrderSpecifier scheduleSort(Pageable pageable) {
        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "movie-title":
                        return new OrderSpecifier(direction, movie.title);
                    case "screen-date":
                        return new OrderSpecifier(direction, schedule.screenTime.startDateTime);
                    case "theater-name":
                        return new OrderSpecifier(direction, theater.name);
                    case "audience":
                        return new OrderSpecifier(direction, movie.audience);
                    default:
                        break;
                }
            }
        }
        return new OrderSpecifier(Order.DESC, schedule.createdDate);
    }

}
