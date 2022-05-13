package study.movie.ticket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.member.entity.Member;
import study.movie.schedule.entity.Schedule;
import study.movie.schedule.entity.SeatStatus;
import study.movie.ticket.entity.Ticket;
import study.movie.schedule.dto.request.UpdateSeatRequest;
import study.movie.ticket.dto.condition.TicketSearchCond;
import study.movie.ticket.dto.condition.TicketSortType;
import study.movie.ticket.dto.request.ReserveTicketRequest;
import study.movie.ticket.dto.response.ReserveTicketResponse;
import study.movie.ticket.dto.response.TicketResponse;
import study.movie.global.dto.IdListRequest;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.DomainSpec;
import study.movie.global.paging.PageableDTO;
import study.movie.global.utils.BasicServiceUtils;
import study.movie.member.repository.MemberRepository;
import study.movie.schedule.repository.ScheduleRepository;
import study.movie.ticket.repository.TicketRepository;

import java.util.List;
import java.util.stream.Collectors;

import static study.movie.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketServiceImpl extends BasicServiceUtils implements TicketService {
    private final TicketRepository ticketRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final PaymentService paymentService;
    private final DomainSpec<TicketSortType> spec = new DomainSpec<>(TicketSortType.class);

    @Override
    @Transactional
    public PostIdResponse reserve(ReserveTicketRequest request) {
        // 상영일정, 사용자 조회
        Schedule schedule = scheduleRepository
                .findById(request.getScheduleId())
                .orElseThrow(getExceptionSupplier(SCHEDULE_NOT_FOUND));

        Member member = memberRepository
                .findById(request.getMemberId())
                .orElseThrow(getExceptionSupplier(MEMBER_NOT_FOUND));

        // 티켓 생성
        Ticket ticket = Ticket.reserveTicket()
                .member(member)
                .seats(request.getSeats())
                .schedule(schedule)
                .build();

        // 티켓  저장
        ticketRepository.save(ticket);

        // 좌석 정보 변경
        scheduleRepository.updateSeatStatus(UpdateSeatRequest.from(schedule.getId(), request.getSeats(), SeatStatus.RESERVED));

        return PostIdResponse.of(ticket.getId());
    }

    @Override
    @Transactional
    public void cancelReservation(String reserveNumber, Long memberId) {
        // 티켓, 스케줄 조회
        Ticket ticket = ticketRepository
                .findByReserveNumber(reserveNumber, memberId)
                .orElseThrow(getExceptionSupplier(TICKET_NOT_FOUND));

        Schedule schedule = scheduleRepository
                .findByScheduleNumber(ticket.getScheduleNumber())
                .orElseThrow(getExceptionSupplier(SCHEDULE_NOT_FOUND));

        // 티켓 상태 변경 -> 취소
        ticket.cancelReservation();

        // 좌석 상태 변경 -> EMPTY
        scheduleRepository.updateSeatStatus(UpdateSeatRequest.from(schedule.getId(), ticket.getSeats(), SeatStatus.EMPTY));

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
        return ticketRepository.search(cond, spec.getPageable(pageableDTO))
                .map(TicketResponse::of);
    }

    @Override
    @Transactional
    public void delete(IdListRequest request) {
        ticketRepository.deleteAllByIdInQuery(request.getIds());
    }
}
