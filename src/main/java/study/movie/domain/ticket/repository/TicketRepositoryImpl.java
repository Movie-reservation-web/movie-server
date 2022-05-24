package study.movie.domain.ticket.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import study.movie.domain.theater.entity.ScreenFormat;
import study.movie.domain.ticket.dto.condition.TicketSearchCond;
import study.movie.domain.ticket.entity.Ticket;
import study.movie.domain.ticket.entity.TicketStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;
import static org.springframework.util.StringUtils.hasText;
import static study.movie.domain.theater.entity.QTheater.theater;
import static study.movie.domain.ticket.entity.QTicket.ticket;
import static study.movie.global.utils.DateTimeUtil.*;
import static study.movie.domain.member.entity.QMember.member;
import static study.movie.domain.movie.entity.QMovie.movie;
import static study.movie.domain.ticket.entity.TicketStatus.RESERVED;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepositoryCustom {

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
    public Optional<Ticket> findByReserveNumber(String reserveNumber, Long memberId) {
        return Optional.ofNullable(queryFactory.selectFrom(ticket)
                .where(
                        memberIdEq(memberId),
                        reserveNumberEq(reserveNumber)
                )
                .fetchOne());
    }

    @Override
    public Page<Ticket> search(TicketSearchCond cond, Pageable pageable) {
        List<Ticket> content = getSearchQueryPaging(cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(ticketSort(pageable))
                .fetch();

        JPAQuery<Long> countQuery = getSearchQueryCount(cond);

        return PageableExecutionUtils.getPage(
                content,
                pageable,
                countQuery::fetchOne);
    }

    private JPAQuery<Ticket> getSearchQueryPaging(TicketSearchCond cond) {
        return queryFactory.selectFrom(ticket)
                .join(ticket.movie, movie).fetchJoin()
                .join(ticket.member, member).fetchJoin()
                .where(getSearchPredicts(cond));
    }

    private JPAQuery<Long> getSearchQueryCount(TicketSearchCond cond) {
        return queryFactory.select(ticket.count())
                .from(ticket)
                .leftJoin(ticket.movie, movie)
                .leftJoin(ticket.member, member)
                .where(getSearchPredicts(cond));
    }

    private Predicate[] getSearchPredicts(TicketSearchCond cond) {
        return new Predicate[]{
                reserveNumberEq(cond.getReserveNumber()),
                movieTitleEq(cond.getMovieTitle()),
                theaterNameEq(cond.getTheaterName()),
                screenFormatEq(cond.getScreenFormat()),
                memberNameEq(cond.getMemberName()),
                dateTimeGoe(dailyStartDateTime(cond.getStartDate())),
                dateTimeLt(dailyEndDateTime(cond.getEndDate())),
                ticketStatusEq(cond.getStatus())
        };
    }

    private BooleanExpression ticketIdLt(Long id) {
        return id != null ? ticket.id.lt(id) : null;
    }

    private BooleanExpression memberIdEq(Long id) {
        return id != null ? member.id.eq(id) : null;
    }

    private BooleanExpression movieTitleEq(String title) {
        return hasText(title) ? movie.title.eq(title) : null;
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

    private OrderSpecifier<?>[] ticketSort(Pageable pageable) {
        return pageable.getSort().stream().map(order -> new OrderSpecifier(
                order.getDirection().isAscending() ? ASC : DESC,
                Expressions.path(Ticket.class, ticket, order.getProperty()))
        ).toArray(OrderSpecifier[]::new);
    }
}
