package study.movie.domain.ticket.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.domain.ticket.dto.condition.TicketSearchCond;
import study.movie.domain.ticket.dto.response.TicketResponse;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.IdListRequest;
import study.movie.global.paging.PageableDTO;
import study.movie.domain.ticket.service.TicketService;

import static study.movie.global.constants.ResponseMessage.DELETE_TICKET;
import static study.movie.global.constants.ResponseMessage.READ_TICKET;

@Api(value = "Tickets Admin Controller", tags = "[Admin] Tickets")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/tickets")
public class TicketAdminController {
    private final TicketService ticketService;

    @Operation(summary = "티켓 예매 조회", description = "조건으로 티켓 예매 정보를 조회한다.")
    @GetMapping("/search")
    public ResponseEntity<?> findAll(@RequestBody(required = false) TicketSearchCond cond, PageableDTO pageableDTO) {
        Page<TicketResponse> result = ticketService.search(cond, pageableDTO);
        return CustomResponse.success(READ_TICKET, result);
    }

    @Operation(summary = "티켓 예매 삭제", description = "티켓 예매 id로 티켓 예매 정보를 삭제한다.")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTicketInDB(@RequestBody IdListRequest request) {
        ticketService.delete(request);
        return CustomResponse.success(DELETE_TICKET);
    }
}
