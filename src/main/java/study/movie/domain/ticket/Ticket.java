package study.movie.domain.ticket;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.BaseTimeEntity;
import study.movie.domain.member.Member;
import study.movie.domain.screening.Screening;
import study.movie.domain.seat.Seat;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Ticket extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "ticket_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private String reservedId;

    @OneToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screening_id")
    private Screening screening;

    @Builder
    public Ticket(Member member, Long price, String reservedId, Seat seat, Screening screening) {
        setOwner(member);
        this.price = price;
        this.reservedId = reservedId;
        this.seat = seat;
        this.screening = screening;
    }

    public void setOwner(Member member) {
        this.member = member;
        member.getTickets().add(this);
    }
}
