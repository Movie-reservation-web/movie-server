package study.movie.domain.ticket;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.converter.ticket.SeatArrayConverter;
import study.movie.domain.member.Member;
import study.movie.domain.movie.Movie;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.Seat;
import study.movie.domain.theater.Screen;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

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
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "screen_id")
    private Screen screen;

    @Convert(converter = SeatArrayConverter.class)
    private List<Seat> seats = new ArrayList<>();

    private String reserveNumber;

    private LocalDateTime reserveDateTime;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;


    //==생성 메서드==//
    @Builder
    public Ticket(Member member, Schedule schedule, List<Seat> seats) {
        this.seats = seats;
        this.reserveNumber = createReserveNumber(schedule.getStartTime());
        this.reserveDateTime = schedule.getStartTime();
        this.ticketStatus = TicketStatus.RESERVED;
        setMember(member);
        setMovie(schedule);
        setScreen(schedule);
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

    private void setScreen(Schedule schedule) {
        this.screen = schedule.getScreen();
        schedule.getScreen().getTickets().add(this);
    }

    //==조회 로직==//
    public int getReservedMemberCount(){
        return seats.size();
    }

    //==비즈니스 로직==//
    public void cancelReservation(){
        this.ticketStatus = TicketStatus.CANCEL;
        getMember().getTickets().remove(this);
        getMovie().getTickets().remove(this);
        getScreen().getTickets().remove(this);
    }

    private String createReserveNumber(LocalDateTime dateTime){
        return String.valueOf(dateTime.getYear()).substring(2) +
                String.format("%02d", dateTime.getMonthValue()) +
                UUID.randomUUID().toString().substring(8, 23).toUpperCase(Locale.ROOT);
    }

}
