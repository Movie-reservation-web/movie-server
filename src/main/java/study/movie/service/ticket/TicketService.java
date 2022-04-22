package study.movie.service.ticket;

import study.movie.dto.ticket.ReserveTicketRequest;
import study.movie.dto.ticket.ReserveTicketResponse;

import java.util.List;

public interface TicketService {

    ReserveTicketResponse reserve(ReserveTicketRequest request);

    void cancelReservation(String reserveNumber);

    List<ReserveTicketResponse> getReservedTicket(Long memberId);

    void delete(Long ticketId);
}
