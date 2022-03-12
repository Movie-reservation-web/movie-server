package study.movie.domain.ticket;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.BaseTimeEntity;
import study.movie.domain.member.Member;

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

    @Builder
    public Ticket(Member member, Long price, String reservedId) {
        this.member = member;
        this.price = price;
        this.reservedId = reservedId;
    }
}
