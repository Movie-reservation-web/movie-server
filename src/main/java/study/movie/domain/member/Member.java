package study.movie.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.reserve.Ticket;
import study.movie.global.constants.EntityAttrConst.GenderType;
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
