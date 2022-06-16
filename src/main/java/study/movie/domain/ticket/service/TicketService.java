package study.movie.domain.ticket.service;

import org.springframework.data.domain.Page;
import study.movie.domain.schedule.dto.response.ScheduleMovieResponse;
import study.movie.domain.ticket.dto.condition.TicketSearchCond;
import study.movie.domain.ticket.dto.request.CancelReservationRequest;
import study.movie.domain.ticket.dto.response.ReserveTicketResponse;
import study.movie.domain.ticket.dto.response.TicketResponse;
import study.movie.global.dto.IdListRequest;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.PageableDTO;

import java.util.List;

public interface TicketService {
    /**
     * Api Server
     * <p>
     * 티켓 예매
     * @return
     */
    PostIdResponse reserve(ScheduleMovieResponse.ReserveTicketRequest request);

    /**
     * Api Server
     * <p>
     * 티켓 예매 취소
     */
    void cancelReservation(CancelReservationRequest request);

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
