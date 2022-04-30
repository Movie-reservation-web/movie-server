package study.movie.service.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.converter.ticket.SeatArrayConverter;
import study.movie.domain.payment.PaymentServiceUtil;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.Seat;
import study.movie.domain.schedule.SeatStatus;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.ticket.Ticket;
import study.movie.dto.schedule.condition.UpdateSeatCond;
import study.movie.dto.ticket.TicketSearchCond;
import study.movie.dto.ticket.request.PaymentRequest;
import study.movie.dto.ticket.request.ReserveTicketRequest;
import study.movie.dto.ticket.response.ReserveTicketResponse;
import study.movie.dto.ticket.response.TicketResponse;
import study.movie.global.dto.IdListRequest;
import study.movie.global.page.PageableDTO;
import study.movie.global.utils.BasicServiceUtils;
import study.movie.repository.member.MemberRepository;
import study.movie.repository.schedule.ScheduleRepository;
import study.movie.repository.ticket.TicketRepository;

import java.util.List;
import java.util.stream.Collectors;

import static study.movie.domain.payment.AgeType.ADULT;
import static study.movie.domain.payment.AgeType.TEENAGER;
import static study.movie.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketServiceImpl extends BasicServiceUtils implements TicketService {
    private final SeatArrayConverter converter;
    private final TicketRepository ticketRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final PaymentServiceUtil paymentService;

    @Override
    @Transactional
    public ReserveTicketResponse reserve(ReserveTicketRequest request) {
        // 상영일정, 좌석 조회
        Schedule schedule = scheduleRepository
                .findById(request.getScheduleId())
                .orElseThrow(getExceptionSupplier(SCHEDULE_NOT_FOUND));
        List<Seat> seats = converter.convertToStringSeat(request.getSeats());

        // 티켓 생성
        Ticket savedTicket = Ticket.reserveTicket()
                .member(
                        memberRepository
                                .findById(request.getMemberId())
                                .orElseThrow(getExceptionSupplier(MEMBER_NOT_FOUND))
                )
                .seats(seats)
                .schedule(schedule)
                .build();

        // 좌석 정보 변경
        UpdateSeatCond condition = createUpdateSeatStatusCond(schedule, seats, SeatStatus.RESERVED);
        scheduleRepository.updateSeatStatus(condition);

        // 영화 관람객 추가
        savedTicket.getMovie().addAudience(savedTicket.getSeats().size());

        return ReserveTicketResponse.of(savedTicket);
    }

    @Override
    @Transactional
    public void cancelReservation(String reserveNumber) {
        // 티켓, 스케줄 조회
        Ticket ticket = ticketRepository
                .findByReserveNumber(reserveNumber)
                .orElseThrow(getExceptionSupplier(TICKET_NOT_FOUND));
        Schedule schedule = scheduleRepository
                .findByScheduleNumber(ticket.getScheduleNumber())
                .orElseThrow(getExceptionSupplier(SCHEDULE_NOT_FOUND));

        // 티켓 상태 변경 -> 취소
        ticket.cancelReservation();

        // 좌석 상태 변경 -> EMPTY
        UpdateSeatCond condition = createUpdateSeatStatusCond(schedule, ticket.getSeats(), SeatStatus.EMPTY);
        scheduleRepository.updateSeatStatus(condition);
    }

    @Override
    public int calcPayment(PaymentRequest request, ScreenFormat screenFormat) {
        return paymentService.calcPaymentAmount(ADULT, screenFormat, request.getReservedTime(), request.getAdultCount())
                + paymentService.calcPaymentAmount(TEENAGER, screenFormat, request.getReservedTime(), request.getTeenagerCount());

    }

    @Override
    public List<ReserveTicketResponse> getReservedTicket(Long ticketId, Long memberId) {
        return ticketRepository.findAllTicketByMember(ticketId, memberId).stream()
                .map(ReserveTicketResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    public Long getReservedTicketCount(Long memberId, int year) {
        return ticketRepository.findReservedTicketCountByMember(memberId, year);
    }

    @Override
    @Transactional
    public void deleteTicketHistory(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(getExceptionSupplier(TICKET_NOT_FOUND));
        ticket.deleteReserveHistory();
    }

    @Override
    public Page<TicketResponse> search(TicketSearchCond cond, PageableDTO pageableDTO) {
        return null;
    }

    @Override
    @Transactional
    public void delete(IdListRequest request) {
        ticketRepository.deleteAllByIdInQuery(request.getIds());
    }

    /**
     * 좌석 상태 변경 condition 생성
     */
    private UpdateSeatCond createUpdateSeatStatusCond(Schedule schedule, List<Seat> seats, SeatStatus status) {
        UpdateSeatCond condition = new UpdateSeatCond();
        condition.setScheduleId(schedule.getId());
        condition.setStatus(status);
        condition.setSeats(seats);
        return condition;
    }


}
