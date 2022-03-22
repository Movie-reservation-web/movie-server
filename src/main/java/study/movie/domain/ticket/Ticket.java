package study.movie.domain.ticket;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.member.Member;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.theater.Seat;
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
    private ReserveStatus reserveStatus;


    //==생성 메서드==//
    @Builder
    public Ticket(Member member, Schedule schedule, String seatNumber, String reserveNumber, LocalDateTime reserveDate, ReserveStatus reserveStatus) {
        this.seatNumber = seatNumber;
        this.reserveNumber = reserveNumber;
        this.reserveDate = reserveDate;
        this.reserveStatus = reserveStatus;
        if(member != null) setMember(member);
        if(schedule != null) setSchedule(schedule);
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
        this.reserveStatus = ReserveStatus.CANCEL;
        getMember().getTickets().remove(this);
        getSchedule().getTickets().remove(this);
    }

}
