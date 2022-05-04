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
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.ticket.Ticket;
import study.movie.domain.ticket.TicketStatus;
import study.movie.dto.ticket.condition.TicketSearchCond;
import study.movie.global.utils.BasicRepositoryUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;
import static study.movie.domain.member.QMember.member;
import static study.movie.domain.movie.QMovie.movie;
import static study.movie.domain.theater.QTheater.theater;
import static study.movie.domain.ticket.QTicket.ticket;
import static study.movie.domain.ticket.TicketStatus.RESERVED;
import static study.movie.global.utils.DateTimeUtil.*;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryImpl extends BasicRepositoryUtils implements TicketRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Ticket> findAllTicketByMember(Long ticketId, Long memberId) {
        return queryFactory.selectFrom(ticket)
                .leftJoin(ticket.member, member)
                .where(
                        ticketIdLt(ticketId),
                        memberIdEq(memberId),
                        isReservedTicket()
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
                        memberIdEq(memberId),
                        reserveYearEq(year),
                        isReservedTicket()
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
                movieTitleEq(cond.getMovieTitle()),
                theaterNameEq(cond.getTheaterName()),
                screenFormatEq(cond.getScreenFormat()),
                memberNameEq(cond.getMemberName()),
                dateTimeGoe(dailyStartDateTime(cond.getScreenStartDate())),
                dateTimeLt(dailyEndDateTime(cond.getScreenEndDate())),
                ticketStatusEq(cond.getStatus())
        };
    }

    private BooleanExpression ticketIdLt(Long id) {
        return id != null ? ticket.id.lt(id) : null;
    }

    private BooleanExpression memberIdEq(Long id) {
        return id != null ? member.id.eq(id) : null;
    }

    private BooleanExpression memberNameEq(String title) {
        return hasText(title) ? member.name.eq(title) : null;
    }

    private BooleanExpression theaterNameEq(String name) {
        return hasText(name) ? theater.name.eq(name) : null;
    }

    private BooleanExpression reserveNumberEq(String reserveNumber) {
        return hasText(reserveNumber) ? ticket.reserveNumber.eq(reserveNumber) : null;
    }

    private BooleanExpression reserveYearEq(Integer year) {
        return dateTimeGoe(year).and(dateTimeLt(year));
    }

    private BooleanExpression dateTimeGoe(Integer year) {
        return year != null ? ticket.screenTime.startDateTime.goe(yearlyStartDateTime(year)) : null;
    }

    private BooleanExpression dateTimeLt(Integer year) {
        return year != null ? ticket.screenTime.startDateTime.lt(yearlyEndDateTime(year)) : null;
    }

    private BooleanExpression dateTimeLt(LocalDateTime dateTime) {
        return dateTime != null ? ticket.screenTime.startDateTime.lt(dateTime) : null;
    }

    private BooleanExpression dateTimeGoe(LocalDateTime dateTime) {
        return dateTime != null ? ticket.screenTime.startDateTime.goe(dateTime) : null;
    }

    private BooleanExpression screenFormatEq(ScreenFormat format) {
        return format != null ? ticket.format.eq(format) : null;
    }

    private BooleanExpression isReservedTicket() {
        return ticketStatusEq(RESERVED);
    }

    private BooleanExpression ticketStatusEq(TicketStatus status) {
        return status != null ? ticket.ticketStatus.eq(status) : null;
    }
}
