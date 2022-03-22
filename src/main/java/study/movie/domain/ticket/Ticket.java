package study.movie.domain.ticket;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.member.Member;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.Seat;
import study.movie.global.constants.EntityAttrConst.ReserveStatus;
import study.movie.global.constants.EntityAttrConst.SeatStatus;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static study.movie.global.constants.EntityAttrConst.ReserveStatus.RESERVE;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    private String seatNumber;

    private String reserveNumber;

    private LocalDateTime reserveDate;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    //==생성 메서드==//
    @Builder
    public Ticket(String reserveNumber, LocalDateTime reserveDate, TicketStatus ticketStatus) {
        this.reserveNumber = reserveNumber;
        this.reserveDate = reserveDate;
        this.ticketStatus = ticketStatus;
    }

    public static Ticket createTicket(Member member, Schedule schedule, Seat seat, String reserveNumber, LocalDateTime reserveDate) {
        Ticket ticket = Ticket.builder()
                .reserveDate(reserveDate)
                .reserveNumber(reserveNumber)
                .reserveStatus(RESERVE)
                .build();

        ticket.setMember(member);
        ticket.setSchedule(schedule);
        ticket.setSeat(seat);
        return ticket;
    }

    //==연관관계 메서드==//
    private void setMember(Member member) {
        this.member = member;
        member.getTickets().add(this);
    }

    private void setSchedule(Schedule schedule) {
        this.schedule = schedule;
        schedule.getTickets().add(this);
    }

    private void setSeat(Seat seat) {
        if(getSchedule().getScreen().isAvailableSeat(seat)) {
            this.seatNumber = seat.seatToString();
            getSchedule().getScreen().updateSeatStatus(seat, SeatStatus.RESERVING);
        }
        else throw new IllegalArgumentException("올바르지 않은 자리 입니다.");
    }

    //==비즈니스 로직==//
    public void cancelReserve(){
        this.ticketStatus = TicketStatus.CANCEL;
        getMember().getTickets().remove(this);
        getSchedule().getTickets().remove(this);
    }

}
