package study.movie.controller.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.dto.ticket.ReserveTicketRequest;
import study.movie.dto.ticket.ReserveTicketResponse;
import study.movie.global.dto.Response;
import study.movie.service.ticket.TicketService;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static study.movie.global.constants.ResponseMessage.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
public class TicketController {
    private final TicketService ticketService;

    /**
     * 티켓 예매
     */
    @PostMapping
    public ResponseEntity<?> reserveTicket(@Valid @RequestBody ReserveTicketRequest request) {
        ReserveTicketResponse result = ticketService.reserve(request);
        return Response.success(CREATED, RESERVE_TICKET, result);
    }

    /**
     * 예매 내역 조회
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getTicketsByMember(@PathVariable Long memberId) {
        List<ReserveTicketResponse> result = ticketService.getReservedTicket(memberId);
        return Response.success(READ_TICKET, result);
    }

//    @GetMapping("/cancel")
//        public ResponseEntity<?> cancelReservation(String reservedMember) {
//            ticketService.cancelReservation(reservedMember);
//            return Response.success(CANCEL_TICKET);
//    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<?> deleteTicketInDB(@PathVariable Long ticketId) {
        ticketService.delete(ticketId);
        return Response.success(DELETE_TICKET);
    }


}
