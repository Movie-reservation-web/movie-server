package study.movie.repository.ticket;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.movie.domain.ticket.Ticket;
import study.movie.domain.ticket.TicketStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static study.movie.domain.member.QMember.member;
import static study.movie.domain.ticket.QTicket.ticket;
import static study.movie.domain.ticket.TicketStatus.*;

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
                        memberIdEq(memberId),
                        reservedYearEq(year),
                        ticketStatusEq(RESERVED)
                )
                .fetchOne();
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? member.id.eq(memberId) : null;
    }

    private BooleanExpression ticketIdLt(Long ticketId) {
        return ticketId != null ? ticket.id.lt(ticketId) : null;
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
