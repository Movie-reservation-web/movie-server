package study.movie.service.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.converter.ticket.SeatArrayConverter;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.Seat;
import study.movie.domain.schedule.SeatStatus;
import study.movie.domain.ticket.Ticket;
import study.movie.dto.schedule.condition.UpdateSeatCond;
import study.movie.dto.ticket.ReserveTicketRequest;
import study.movie.dto.ticket.ReserveTicketResponse;
import study.movie.global.utils.BasicServiceUtils;
import study.movie.global.enumMapper.EnumMapper;
import study.movie.repository.member.MemberRepository;
import study.movie.repository.schedule.ScheduleRepository;
import study.movie.repository.ticket.TicketRepository;

import java.util.List;
import java.util.stream.Collectors;

import static study.movie.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketServiceImpl extends BasicServiceUtils implements TicketService {
    private final EnumMapper enumMapper;
    private final SeatArrayConverter converter;
    private final TicketRepository ticketRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    /**
     * 티켓 예매
     */
    @Transactional
    public ReserveTicketResponse reserve(ReserveTicketRequest request) {
        // 상영일정, 좌석 조회
        Schedule schedule = scheduleRepository
                .findById(request.getScheduleId())
                .orElseThrow(getExceptionSupplier(SCHEDULE_NOT_FOUND));
        List<Seat> seats = converter.convertToStringSeat(request.getSeats());

        // 티켓 생성
        Ticket savedTicket = Ticket.builder()
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

        return new ReserveTicketResponse(enumMapper, savedTicket);
    }

    /**
     * 예매 취소
     */
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

    /**
     * 티켓 조회(사용자)
     * @param memberId
     */
    public List<ReserveTicketResponse> getAllTicket(Long memberId){
        return ticketRepository.findAllTicketByMember(memberId).stream()
                .map(ticket -> new ReserveTicketResponse(enumMapper, ticket))
                .collect(Collectors.toList());
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
