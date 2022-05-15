package study.movie.ticket.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.member.entity.Member;
import study.movie.movie.entity.Movie;
import study.movie.schedule.entity.Schedule;
import study.movie.schedule.entity.ScreenTime;
import study.movie.theater.entity.ScreenFormat;
import study.movie.global.converter.StringArrayConverter;
import study.movie.global.entity.BaseTimeEntity;
import study.movie.exception.CustomException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static study.movie.exception.ErrorCode.ALREADY_CANCELLED_TICKET;

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

    private Integer price;

    @Convert(converter = StringArrayConverter.class)
    private List<String> seats = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    //==생성 메서드==//
    @Builder(builderClassName = "reserveTicket", builderMethodName = "reserveTicket")
    public Ticket(Member member, Schedule schedule, List<String> seats) {
        this.theaterName = schedule.getScreen().getTheater().getName();
        this.screenName = schedule.getScreen().getName();
        this.screenTime = schedule.getScreenTime();
        this.reserveNumber = createReserveNumber(schedule.getScreenTime().getStartDateTime());
        this.seats = seats;
        this.ticketStatus = TicketStatus.RESERVED;
        this.format = schedule.getScreen().getFormat();
        setMember(member);
        setMovie(schedule);
        setScheduleNumber(schedule);
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

    public void setScheduleNumber(Schedule schedule) {
        this.scheduleNumber = schedule.getScheduleNumber();
        // 예약된 좌석수 추가
        schedule.addReservedSeatCount(seats.size());
    }

    //==조회 로직==//
    public int getReservedMemberCount() {
        return seats.size();
    }

    //==비즈니스 로직==//

    /**
     * 예매 취소
     */
    public void cancelReservation() {
        if (this.ticketStatus != TicketStatus.RESERVED) throw new CustomException(ALREADY_CANCELLED_TICKET);
        getMember().getTickets().remove(this);
        getMovie().getTickets().remove(this);
        this.ticketStatus = TicketStatus.CANCEL;
    }

    /**
     * 내가 본 영화 삭제
     */
    public void deleteReserveHistory() {
        if (this.ticketStatus != TicketStatus.RESERVED) throw new CustomException(ALREADY_CANCELLED_TICKET);
        getMember().getTickets().remove(this);
        this.ticketStatus = TicketStatus.DELETE;
    }

    /**
     * 예매 번호 생성
     */
    private String createReserveNumber(LocalDateTime dateTime) {
        return String.valueOf(dateTime.getYear()).substring(2) +
                String.format("%02d", dateTime.getMonthValue()) +
                UUID.randomUUID().toString().substring(8, 23).toUpperCase(Locale.ROOT);
    }
}
