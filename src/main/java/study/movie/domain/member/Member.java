package study.movie.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.BaseTimeEntity;
import study.movie.domain.ticket.Ticket;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate birth;

    @OneToMany(mappedBy = "member")
    private List<Ticket> tickets = new ArrayList<>();

    @Builder
    public Member(String name, String email, LocalDate birth) {
        this.name = name;
        this.email = email;
        this.birth = birth;
    }
}