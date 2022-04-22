package study.movie.domain.ticket;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.converter.ticket.SeatArrayConverter;
import study.movie.domain.member.Member;
import study.movie.domain.movie.Movie;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.ScreenTime;
import study.movie.domain.schedule.Seat;
import study.movie.domain.theater.ScreenFormat;
import study.movie.global.entity.BaseTimeEntity;
import study.movie.global.exception.CustomException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static study.movie.global.exception.ErrorCode.ALREADY_CANCELLED_TICKET;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    private String reserveNumber;
    private String scheduleNumber;

    private String theaterName;
    private String screenName;
    private ScreenFormat format;

    private ScreenTime screenTime;

    @Convert(converter = SeatArrayConverter.class)
    private List<Seat> seats = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    //==생성 메서드==//
    @Builder
    public Ticket(Member member, Schedule schedule, List<Seat> seats) {
        this.reserveNumber = createReserveNumber(schedule.getScreenTime().getStartDateTime());
        this.scheduleNumber = schedule.getScheduleNumber();
        this.theaterName = schedule.getScreen().getTheater().getName();
        this.screenName = schedule.getScreen().getName();
        this.screenTime = schedule.getScreenTime();
        this.seats = seats;
        this.ticketStatus = TicketStatus.RESERVED;
        this.format = schedule.getScreen().getFormat();
        setMember(member);
        setMovie(schedule);
    }

    //==연관관계 메서드==//
    private void setMember(Member member) {
        this.member = member;
        member.getTickets().add(this);
    }

    private void setMovie(Schedule schedule) {
        this.movie = schedule.getMovie();
        schedule.getMovie().getTickets().add(this);
    }

    public void deleteTicket(){
        getMember().getTickets().remove(this);
        getMovie().getTickets().remove(this);
    }

    //==조회 로직==//
    public int getReservedMemberCount(){
        return seats.size();
    }

    //==비즈니스 로직==//
    /**
     * 예매 취소
     */
    public void cancelReservation(){
        if (this.ticketStatus == TicketStatus.CANCEL) throw new CustomException(ALREADY_CANCELLED_TICKET);
        this.ticketStatus = TicketStatus.CANCEL;
    }

    /**
     * 예매 번호 생성
     */
    private String createReserveNumber(LocalDateTime dateTime){
        return String.valueOf(dateTime.getYear()).substring(2) +
                String.format("%02d", dateTime.getMonthValue()) +
                UUID.randomUUID().toString().substring(8, 23).toUpperCase(Locale.ROOT);
    }

}
