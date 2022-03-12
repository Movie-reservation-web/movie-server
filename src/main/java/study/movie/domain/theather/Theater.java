package study.movie.domain.theather;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.BaseTimeEntity;
import study.movie.domain.screen.Screen;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Theater extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "theater_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String city;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @OneToMany(mappedBy = "theater")
    private List<Screen> screenList = new ArrayList<>();

    @Builder
    public Theater(String city, String name, String phone) {
        this.city = city;
        this.name = name;
        this.phone = phone;
    }
}
