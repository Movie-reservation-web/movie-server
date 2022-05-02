package study.movie.repository.ticket;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.ticket.Ticket;
import study.movie.domain.ticket.TicketStatus;
import study.movie.dto.ticket.TicketSearchCond;
import study.movie.global.utils.BasicRepositoryUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;
import static study.movie.domain.member.QMember.member;
import static study.movie.domain.movie.QMovie.movie;
import static study.movie.domain.schedule.QSchedule.schedule;
import static study.movie.domain.theater.QScreen.screen;
import static study.movie.domain.theater.QTheater.theater;
import static study.movie.domain.ticket.QTicket.ticket;
import static study.movie.domain.ticket.TicketStatus.RESERVED;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryImpl extends BasicRepositoryUtils implements TicketRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Ticket> findAllTicketByMember(Long ticketId, Long memberId) {
        return queryFactory.selectFrom(ticket)
                .leftJoin(ticket.member, member)
                .where(
                        entityIdLt(ticket.id, ticketId),
                        entityIdEq(member.id, memberId),
                        ticketStatusEq(RESERVED)
                )
                .orderBy(ticket.id.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public Long findReservedTicketCountByMember(Long memberId, int year) {
        return queryFactory.select(ticket.count())
                .from(ticket)
                .where(
                        entityIdEq(member.id, memberId),
                        reservedYearEq(year),
                        ticketStatusEq(RESERVED)
                )
                .fetchOne();
    }

    @Override
    public Page<Ticket> search(TicketSearchCond cond, Pageable pageable) {
        List<Ticket> content = getTicketJPAQueryPaging()
                .where(getSearchPredicts(cond))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(ticket.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = getTicketJPAQueryCount()
                .where(getSearchPredicts(cond));

        return PageableExecutionUtils.getPage(
                content,
                pageable,
                countQuery::fetchOne);
    }

    private JPAQuery<Schedule> getTicketJPAQueryFetch() {
        return queryFactory.selectFrom(schedule)
                .join(schedule.movie, movie).fetchJoin()
                .join(schedule.screen, screen).fetchJoin()
                .join(screen.theater, theater).fetchJoin();
    }

    private JPAQuery<Ticket> getTicketJPAQueryPaging() {
        return queryFactory.selectFrom(ticket)
                .leftJoin(ticket.movie, movie)
                .leftJoin(ticket.member, member);
    }

    private JPAQuery<Long> getTicketJPAQueryCount() {
        return queryFactory.select(ticket.count())
                .from(ticket)
                .leftJoin(ticket.movie, movie)
                .leftJoin(ticket.member, member);
    }

    private Predicate[] getSearchPredicts(TicketSearchCond cond) {
        return new Predicate[]{
                reserveNumberEq(cond.getReserveNumber()),
                scheduleNumberEq(cond.getScheduleNumber()),
                memberNameEq(cond.getMemberName()),
                movieTitleEq(cond.getMovieTitle()),
                ticketStatusEq(cond.getTicketStatus()),
                screenFormatEq(ticket.format, cond.getFormat()),
                dateAfter(ticket.screenTime.startDateTime, cond.getScreenDateStart()),
                dateBefore(ticket.screenTime.startDateTime, cond.getScreenDateEnd()),
        };
    }

    private BooleanExpression reserveNumberEq(String reserveNumber) {
        return hasText(reserveNumber) ? ticket.reserveNumber.eq(reserveNumber) : null;
    }

    private BooleanExpression scheduleNumberEq(String scheduleNumber) {
        return hasText(scheduleNumber) ? ticket.scheduleNumber.eq(scheduleNumber) : null;
    }

    private BooleanExpression reservedYearEq(Integer year) {
        return year != null ? ticket.screenTime.startDateTime.between(
                LocalDateTime.of(LocalDate.ofYearDay(year, LocalDate.MIN.getDayOfYear()), LocalTime.MIN).withNano(0),
                LocalDateTime.of(LocalDate.ofYearDay(year, LocalDate.MAX.getDayOfYear()), LocalTime.MAX).withNano(0)
        ) : null;
    }

    private BooleanExpression ticketStatusEq(TicketStatus status) {
        return status != null ? ticket.ticketStatus.eq(status) : null;
    }
}
