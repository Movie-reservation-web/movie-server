package study.movie.domain.reserve;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.member.Member;
import study.movie.domain.screen.Schedule;
import study.movie.domain.screen.Seat;
import study.movie.global.constants.EntityAttrConst.ReserveStatus;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

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

    @Embedded
    private Seat seat;

    private String reserveNumber;

    private LocalDateTime reserveDate;

    @Enumerated(EnumType.STRING)
    private ReserveStatus status;

    //==생성 메서드==//
    @Builder
    public Ticket(Seat seat, String reserveNumber, LocalDateTime reserveDate) {
        this.seat = seat;
        this.reserveNumber = reserveNumber;
        this.reserveDate = reserveDate;
        this.status = ReserveStatus.RESERVE;
    }

    public static Ticket createTicket(Member member, Schedule schedule, Seat seat, String reserveNumber, LocalDateTime reserveDate) {
        Ticket ticket = Ticket.builder()
                .reserveDate(reserveDate)
                .reserveNumber(reserveNumber)
                .seat(seat)
                .build();
        ticket.setMember(member);
        ticket.setSchedule(schedule);
        return ticket;
    }

    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getTickets().add(this);
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
        schedule.getTickets().add(this);
    }
}
