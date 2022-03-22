package study.movie.domain.ticket;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.member.Member;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.Seat;
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

    @OneToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    private String reserveNumber;

    private LocalDateTime reserveDate;

    @Enumerated(EnumType.STRING)
    private ReserveStatus reserveStatus;

    //==생성 메서드==//
    @Builder
    public Ticket(Member member, Schedule schedule, Seat seat, String reserveNumber, LocalDateTime reserveDate, ReserveStatus reserveStatus) {
        this.member = member;
        this.schedule = schedule;
        this.seat = seat;
        this.reserveNumber = reserveNumber;
        this.reserveDate = reserveDate;
        this.reserveStatus = reserveStatus;
        this.setMember(member);
        this.setSchedule(schedule);
    }

    //==연관관계 메서드==//
    private void setMember(Member member) {
        if (member == null) {
            return;
        }
        this.member = member;
        member.getTickets().add(this);
    }

    private void setSchedule(Schedule schedule) {
        if (schedule == null) {
            return;
        }
        this.schedule = schedule;
        schedule.getTickets().add(this);
    }

    //==비즈니스 로직==//
    public void cancelReserve(){
        this.reserveStatus = ReserveStatus.CANCEL;
        getMember().getTickets().remove(this);
        getSchedule().getTickets().remove(this);
    }
}
