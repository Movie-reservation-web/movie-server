package study.movie.domain.schedule.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import study.movie.domain.schedule.dto.condition.ScheduleBasicSearchCond;
import study.movie.domain.schedule.entity.ScheduleStatus;
import study.movie.global.utils.BasicRepositoryUtil;
import study.movie.domain.schedule.dto.condition.ScheduleSearchCond;
import study.movie.domain.schedule.dto.request.ScheduleScreenRequest;
import study.movie.domain.schedule.dto.request.UpdateSeatRequest;
import study.movie.domain.schedule.entity.Schedule;
import study.movie.domain.schedule.entity.Seat;
import study.movie.domain.schedule.entity.SeatEntity;
import study.movie.domain.theater.entity.ScreenFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;
import static study.movie.domain.schedule.entity.QSchedule.schedule;
import static study.movie.domain.schedule.entity.QSeatEntity.seatEntity;
import static study.movie.domain.theater.entity.QScreen.screen;
import static study.movie.domain.theater.entity.QTheater.theater;
import static study.movie.global.utils.DateTimeUtil.dailyEndDateTime;
import static study.movie.global.utils.DateTimeUtil.dailyStartDateTime;
import static study.movie.domain.movie.entity.QMovie.movie;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ScheduleRepositoryImpl extends BasicRepositoryUtil implements ScheduleRepositoryCustom {

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
    public Optional<Schedule> findDuplicatedSchedule(Long screenId, LocalDateTime startTime, LocalDateTime endTime) {
        return Optional.ofNullable(
                queryFactory.selectFrom(schedule)
                        .join(schedule.screen, screen).fetchJoin()
                        .where(
                                screen.id.eq(screenId),
                                startDateTimeBetween(startTime, endTime),
                                endDateTimeBetween(startTime, endTime)
                        )
                        .fetchFirst()
        );
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
                        theater.id.in(request.getTheaterIds())
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
    public List<SeatEntity> findSeatsByScheduleId(Long scheduleId) {
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
                        scheduleStatusEq(ScheduleStatus.OPEN)
                )
                .distinct()
                .fetch();
    }

    @Override
    public Page<Schedule> search(ScheduleSearchCond cond, Pageable pageable) {
        List<Schedule> elements = getSearchElementsQuery(cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(mapToOrderSpec(pageable.getSort(), Schedule.class, schedule))
                .fetch();

        JPAQuery<Long> countQuery = getSearchCountQuery(cond);

        return PageableExecutionUtils.getPage(
                elements,
                pageable,
                countQuery::fetchOne);
    }

    @Override
    public void updateStatusByPastDaysStatus(LocalDateTime dateTime) {
        queryFactory.update(schedule)
                .set(schedule.status, ScheduleStatus.CLOSED)
                .where(
                        dateTimeLt(dateTime),
                        scheduleStatusNe(ScheduleStatus.CLOSED)
                );
    }

    private JPAQuery<Schedule> getSearchElementsQuery(ScheduleSearchCond cond) {
        return queryFactory.selectFrom(schedule)
                .join(schedule.movie, movie).fetchJoin()
                .join(schedule.screen, screen).fetchJoin()
                .join(screen.theater, theater).fetchJoin()
                .where(getSearchPredicts(cond));
    }

    private JPAQuery<Long> getSearchCountQuery(ScheduleSearchCond cond) {
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
                dateTimeGoe(dailyStartDateTime(cond.getStartDate())),
                dateTimeLt(dailyEndDateTime(cond.getEndDate())),
                screenFormatEq(cond.getScreenFormat()),
                scheduleStatusEq(cond.getStatus())
        };
    }

    private BooleanExpression scheduleStatusEq(ScheduleStatus status) {
        return status != null ? schedule.status.eq(status) : null;
    }

    private BooleanExpression scheduleStatusNe(ScheduleStatus status) {
        return status != null ? schedule.status.ne(status) : null;
    }

    private BooleanExpression movieTitleEq(String title) {
        return hasText(title) ? movie.title.eq(title) : null;
    }

    private BooleanExpression screenFormatEq(ScreenFormat format) {
        return format != null ? screen.format.eq(format) : null;
    }

    private BooleanExpression scheduleIdEq(Long scheduleId) {
        return scheduleId != null ? seatEntity.schedule.id.eq(scheduleId) : null;
    }

    private BooleanExpression dateTimeLt(LocalDateTime dateTime) {
        return dateTime != null ? schedule.screenTime.startDateTime.lt(dateTime) : null;
    }

    private BooleanExpression dateTimeGoe(LocalDateTime dateTime) {
        return dateTime != null ? schedule.screenTime.startDateTime.goe(dateTime) : null;
    }

    private BooleanExpression startDateTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime != null && endTime != null ? dateTimeGoe(startTime).and(dateTimeLt(endTime)) : null;
    }

    private BooleanExpression endDateTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime != null && endTime != null ?
                schedule.screenTime.endDateTime.goe(startTime).and(
                        schedule.screenTime.endDateTime.lt(endTime))
                : null;
    }

    private BooleanExpression seatsIn(List<Seat> seats) {
        return seats != null ? seatEntity.seat.in(seats) : null;
    }
}
