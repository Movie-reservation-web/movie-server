package study.movie.domain.theater;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Theater extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id")
    private Long id;

    private String name;
    private String city;
    private String phone;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL)
    private List<Screen> screens = new ArrayList<>();

    @Builder
    public Theater(String name, String city, String phone) {
        this.name = name;
        this.city = city;
        this.phone = phone;
    }
}
