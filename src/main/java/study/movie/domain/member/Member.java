package study.movie.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.ticket.Ticket;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private LocalDate birth;

    private String nickname;

    private String email;

    private GenderType gender;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Ticket> tickets = new ArrayList<>();

    //==생성 메서드==//
    @Builder
    public Member(String name, LocalDate birth, String nickname, String email, GenderType gender) {
        this.name = name;
        this.birth = birth;
        this.nickname = nickname;
        this.email = email;
        this.gender = gender;
    }
}
