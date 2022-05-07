package study.movie.controller.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.dto.ticket.request.ReserveTicketRequest;
import study.movie.dto.ticket.response.ReserveTicketResponse;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.service.ticket.TicketService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static study.movie.global.constants.ResponseMessage.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
public class TicketApiController {
    private final TicketService ticketService;

    /**
     * 티켓 예매
     */
    @PostMapping
    public ResponseEntity<?> reserveTicket(@Valid @RequestBody ReserveTicketRequest request) {
        PostIdResponse result = ticketService.reserve(request);
        return CustomResponse.success(CREATED, RESERVE_TICKET, result);
    }

    /**
     * 예매 내역 조회
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getTicketsByMember(@PathVariable Long memberId,
                                                @RequestParam Long ticketId) {
        List<ReserveTicketResponse> result = ticketService.getReservedTicket(ticketId, memberId);
        return CustomResponse.success(READ_TICKET, result);
    }

    /**
     * 예매 내역 조회 - 전체 갯수
     */
    @GetMapping("/count/{memberId}")
    public ResponseEntity<?> getReservedTicketCount(@PathVariable Long memberId,
                                                    @RequestParam(required = false) int year) {
        Long result = ticketService.getReservedTicketCount(memberId, year);
        return CustomResponse.success(READ_TICKET_COUNT, result);
    }

    /**
     * 내가 본 영화에서 삭제
     */
    @PostMapping("/{id}")
    public ResponseEntity<?> deleteReservedTicketHistory(@PathVariable Long id) {
        ticketService.deleteTicketHistory(id);
        return CustomResponse.success(DELETE_TICKET_HISTORY);
    }

    /**
     * 예매 취소
     */
    @GetMapping("/cancel")
    public ResponseEntity<?> cancelReservation(String reserveNumber, Long memberId) {
        ticketService.cancelReservation(reserveNumber, memberId);
        return CustomResponse.success(CANCEL_TICKET);
    }


}
