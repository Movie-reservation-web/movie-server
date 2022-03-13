package study.movie.app.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.app.reserve.entity.Ticket;
import study.movie.global.constants.EntityAttrConst.GenderType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "member",  cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    private String name;

    private LocalDate birth;

    private String nickname;

    private String email;

    private GenderType gender;
}
