package study.movie.controller.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.dto.ticket.condition.TicketSearchCond;
import study.movie.dto.ticket.response.TicketResponse;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.IdListRequest;
import study.movie.global.paging.PageableDTO;
import study.movie.service.ticket.TicketService;

import static study.movie.global.constants.ResponseMessage.DELETE_TICKET;
import static study.movie.global.constants.ResponseMessage.READ_TICKET;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/tickets")
public class TicketAdminController {
    private final TicketService ticketService;

    @GetMapping("/search")
    public ResponseEntity<?> findAll(@RequestBody(required = false) TicketSearchCond cond, PageableDTO pageableDTO) {
        Page<TicketResponse> result = ticketService.search(cond, pageableDTO);
        return CustomResponse.success(READ_TICKET, result);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTicketInDB(@RequestBody IdListRequest request) {
        ticketService.delete(request);
        return CustomResponse.success(DELETE_TICKET);
    }


}
