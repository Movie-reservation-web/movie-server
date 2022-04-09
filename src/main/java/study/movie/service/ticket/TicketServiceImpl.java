package study.movie.service.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.schedule.Seat;
import study.movie.domain.ticket.Ticket;
import study.movie.dto.ticket.ReserveTicketRequest;
import study.movie.dto.ticket.ReserveTicketResponse;
import study.movie.global.enumMapper.EnumMapper;
import study.movie.repository.member.MemberRepository;
import study.movie.repository.schedule.ScheduleRepository;
import study.movie.repository.ticket.TicketRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketServiceImpl implements TicketService {
    private final EnumMapper enumMapper;
    private final TicketRepository ticketRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public ReserveTicketResponse reserve(ReserveTicketRequest request) {
        Ticket result = Ticket.builder()
                .member(memberRepository.findById(request.getMemberId()).orElseThrow(IllegalArgumentException::new))
                .seats(request.getSeats().stream().map(Seat::stringToSeat).collect(Collectors.toList()))
                .schedule(scheduleRepository.findById(request.getScheduleId()).orElseThrow(IllegalArgumentException::new))
                .build();

        return new ReserveTicketResponse(enumMapper, result.getMovie(), result.getScreen());
    }
}
