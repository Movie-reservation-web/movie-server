package study.movie.domain.ticket.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.domain.schedule.dto.response.ScheduleMovieResponse;
import study.movie.domain.ticket.dto.request.CancelReservationRequest;
import study.movie.domain.ticket.dto.response.ReserveTicketResponse;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.domain.ticket.service.TicketService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static study.movie.global.constants.ResponseMessage.*;


@Api(value = "Tickets Api Controller", tags = "[Api] Tickets")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
@Slf4j
public class TicketApiController {
    private final TicketService ticketService;

    /**
     * 티켓 예매
     */
    @Operation(summary = "티켓 예매 생성", description = "티켓 예매 정보를 저장한다.")
    @PostMapping
    public ResponseEntity<?> reserveTicket(@Valid @RequestBody ScheduleMovieResponse.ReserveTicketRequest request) {
        PostIdResponse result = ticketService.reserve(request);
        return CustomResponse.success(CREATED, RESERVE_TICKET, result);
    }

    /**
     * 예매 내역 조회
     */
    @Operation(summary = "티켓(회원 id) 조회", description = "회원 id, 티켓 id로 티켓 정보를 검색한다.")
    @Parameters({@Parameter(name = "id", description = "회원의 id", required = true, in = ParameterIn.PATH)})
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getTicketsByMember(@PathVariable Long memberId,
                                                @RequestParam Long ticketId) {
        List<ReserveTicketResponse> result = ticketService.getReservedTicket(ticketId, memberId);
        return CustomResponse.success(READ_TICKET, result);
    }

    /**
     * 예매 내역 조회 - 전체 갯수
     */
    @Operation(summary = "티켓 내역 건수 조회", description = "회원 id로 티켓 에매 건수를 조회한다.")
    @Parameters({@Parameter(name = "id", description = "회원의 id", required = true, in = ParameterIn.PATH)})
    @GetMapping("/count/{memberId}")
    public ResponseEntity<?> getReservedTicketCount(@PathVariable Long memberId,
                                                    @RequestParam(required = false) int year) {
        Long result = ticketService.getReservedTicketCount(memberId, year);
        return CustomResponse.success(READ_TICKET_COUNT, result);
    }

    /**
     * 내가 본 영화에서 삭제
     */
    @Operation(summary = "티켓 삭제", description = "티켓 id로 티켓 정보를 삭제한다.")
    @Parameters({@Parameter(name = "id", description = "티켓의 id", required = true, in = ParameterIn.PATH)})
    @PostMapping("/{id}")
    public ResponseEntity<?> deleteReservedTicketHistory(@PathVariable Long id) {
        ticketService.deleteTicketHistory(id);
        return CustomResponse.success(DELETE_TICKET_HISTORY);
    }

    /**
     * 예매 취소
     */
    @Operation(summary = "티켓 예매 취소", description = "티켓 예매를 취소한다.")
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelReservation(@RequestBody CancelReservationRequest request) {
        ticketService.cancelReservation(request);
        return CustomResponse.success(CANCEL_TICKET);
    }
}
