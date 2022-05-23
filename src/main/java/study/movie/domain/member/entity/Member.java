package study.movie.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.global.entity.BaseTimeEntity;
import study.movie.domain.ticket.entity.Ticket;

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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(unique = true, updatable = false)
    private String email;

    @Column(updatable = false)
    private String name;

    @Column(updatable = false)
    private LocalDate birth;

    private String nickname;

    private GenderType gender;

    @Column(unique = true)
    private String mobile;

    private String profileImg;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Ticket> tickets = new ArrayList<>();

    //==생성 메서드==//
    @Builder(builderClassName = "basicBuilder", builderMethodName = "basicBuilder")
    public Member(String email, String password, String name, String mobile, String nickname, LocalDate birth, GenderType gender, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.mobile = mobile;
        this.nickname = nickname;
        this.birth = birth;
        this.gender = gender;
        this.role = role;
    }

    @Builder(builderClassName = "socialBuilder", builderMethodName = "socialBuilder")
    public Member(String email, String name, SocialType socialType) {
        this.email = email;
        this.name = name;
        this.socialType = socialType;
        this.role = Role.GUEST;
    }

    //== 업데이트 로직==//
    public void updateProfile(String image) {
        this.profileImg = image;
    }

    public void connectSocial(SocialType socialType) {
        this.socialType = socialType;
    }

    public void conferRole(Role role) {
        this.role = role;
    }
}
