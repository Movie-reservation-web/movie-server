package study.movie.domain.ticket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.movie.domain.ticket.dto.condition.TicketSearchCond;
import study.movie.domain.ticket.dto.request.CancelReservationRequest;
import study.movie.domain.ticket.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepositoryCustom {
    // API

    /**
     * 예매 내역 조회 - 더보기 방식의 페이징, 회원 메뉴.
     * @param ticketId
     * @param memberId
     * @return List
     */
    List<Ticket> findAllTicketByMember(Long ticketId, Long memberId);

    /**
     * 예매 내역 조회(갯수) - 하단에 표기할 전체 예매 내역 수 조회
     * @param memberId
     * @param year
     * @return Long
     */
    Long findReservedTicketCountByMember(Long memberId, int year);

    /**
     * 예매 정보 조회(예매 취소시 사용)
     * @return
     */
    Optional<Ticket> findByReserveNumber(CancelReservationRequest request);

    // ADMIN

    /**
     * 예매 내역 조회
     * @param cond reserveNumber, movieTitle, theaterName, screenFormat, memberName, screenDateRange
     *             TicketStatus
     * @param pageable
     * @return Page
     */
    Page<Ticket> search(TicketSearchCond cond, Pageable pageable);
}
