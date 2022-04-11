package study.movie.service.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.converter.ticket.SeatArrayConverter;
import study.movie.domain.schedule.SeatStatus;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.Seat;
import study.movie.domain.ticket.Ticket;
import study.movie.dto.schedule.UpdateSeatCond;
import study.movie.dto.ticket.ReserveTicketRequest;
import study.movie.dto.ticket.ReserveTicketResponse;
import study.movie.global.enumMapper.EnumMapper;
import study.movie.repository.member.MemberRepository;
import study.movie.repository.schedule.ScheduleRepository;
import study.movie.repository.ticket.TicketRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketServiceImpl implements TicketService {
    private final EnumMapper enumMapper;
    private final SeatArrayConverter converter;
    private final TicketRepository ticketRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public ReserveTicketResponse reserve(ReserveTicketRequest request) {
        // 상영일정, 좌석 조회
        Schedule schedule = scheduleRepository.findById(request.getScheduleId()).orElseThrow(IllegalArgumentException::new);
        List<Seat> seats = converter.convertToStringSeat(request.getSeats());

        // 티켓 생성
        Ticket savedTicket = Ticket.builder()
                .member(memberRepository.findById(request.getMemberId()).orElseThrow(IllegalArgumentException::new))
                .seats(seats)
                .schedule(schedule)
                .build();

        // 좌석 정보 변경
        UpdateSeatCond condition = createUpdateSeatStatusCond(schedule, seats, SeatStatus.RESERVED);
        scheduleRepository.updateSeatStatus(condition);

        return new ReserveTicketResponse(enumMapper, savedTicket);
    }

    @Transactional
    public void cancelReservation(String reserveNumber) {
        // 티켓, 스케줄 조회
        Ticket ticket = ticketRepository.findByReserveNumber(reserveNumber).orElseThrow(IllegalArgumentException::new);
        Schedule schedule = scheduleRepository.findByScheduleNumber(ticket.getScheduleNumber()).orElseThrow();

        // 티켓 상태 변경 -> 취소
        ticket.cancelReservation();

        // 좌석 상태 변경 -> EMPTY
        UpdateSeatCond condition = createUpdateSeatStatusCond(schedule, ticket.getSeats(), SeatStatus.EMPTY);
        scheduleRepository.updateSeatStatus(condition);
    }

    private UpdateSeatCond createUpdateSeatStatusCond(Schedule schedule, List<Seat> seats, SeatStatus status) {
        UpdateSeatCond condition = new UpdateSeatCond();
        condition.setScheduleId(schedule.getId());
        condition.setStatus(status);
        condition.setSeats(seats);
        return condition;
    }
}
