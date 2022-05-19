package study.movie.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.global.entity.BaseTimeEntity;
import study.movie.ticket.entity.Ticket;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String email;

    private String name;

    private LocalDate birth;

    private String nickname;

    private GenderType gender;

    private String phone;

    private String profileImg;

    @ElementCollection(fetch = EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Ticket> tickets = new ArrayList<>();

    //==생성 메서드==//
    @Builder
    public Member(String email, String password, String name, String phone, String nickname, LocalDate birth, GenderType gender, List<String> roles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.nickname = nickname;
        this.birth = birth;
        this.gender = gender;
        this.roles = roles;
    }

    //== 업데이트 로직==//
    public void updateProfile(String image) {
        this.profileImg = image;
    }
}
