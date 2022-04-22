package study.movie.repository.ticket;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.movie.domain.ticket.Ticket;

import java.util.List;

import static study.movie.domain.member.QMember.member;
import static study.movie.domain.ticket.QTicket.ticket;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Ticket> findAllTicketByMember(Long memberId) {
        return queryFactory.selectFrom(ticket)
                .leftJoin(ticket.member, member)
                .where(memberIdEq(memberId))
                .fetch();
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? member.id.eq(memberId) : null;
    }
}
