package study.movie.service.ticket;

import org.springframework.data.domain.Page;
import study.movie.domain.theater.ScreenFormat;
import study.movie.dto.ticket.condition.TicketSearchCond;
import study.movie.dto.ticket.request.PaymentRequest;
import study.movie.dto.ticket.request.ReserveTicketRequest;
import study.movie.dto.ticket.response.ReserveTicketResponse;
import study.movie.dto.ticket.response.TicketResponse;
import study.movie.global.dto.IdListRequest;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.page.PageableDTO;

import java.util.List;

public interface TicketService {
    /**
     * Api Server
     * <p>
     * 티켓 예매
     * @return
     */
    PostIdResponse reserve(ReserveTicketRequest request);

    /**
     * Api Server
     * <p>
     * 티켓 예매 취소
     */
    void cancelReservation(String reserveNumber);

    /**
     * Api Server
     * <p>
     * 결제 서비스
     */
    int calcPayment(PaymentRequest request, ScreenFormat screenFormat);

    /**
     * Api Server
     * <p>
     * 사용자 예매 티켓 내역 조회
     */
    List<ReserveTicketResponse> getReservedTicket(Long ticketId, Long memberId);

    /**
     * Api Server
     * <p>
     * 사용자 예매 티켓 수 조회
     */
    Long getReservedTicketCount(Long memberId, int year);

    /**
     * Api Server
     * <p>
     * 사용자가 본 티켓 내역 제거(논리 삭제)
     */
    void deleteTicketHistory(Long id);

    /**
     * Admin Server
     * <p>
     * 모든 예매 내역 조회
     */
    Page<TicketResponse> search(TicketSearchCond cond, PageableDTO pageableDTO);

    /**
     * Admin Server
     * <p>
     * 티켓 데이터 삭제(물리 삭제)
     */
    void delete(IdListRequest request);

}
