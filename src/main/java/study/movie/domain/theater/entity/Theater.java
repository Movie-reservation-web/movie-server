package study.movie.domain.theater.entity;

import lombok.*;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "screens")
public class Theater extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id")
    private Long id;

    private String name;

    private CityCode city;

    private String phone;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.PERSIST)
    private List<Screen> screens = new ArrayList<>();

    //==생성 메서드==//
    @Builder
    public Theater(String name, CityCode city, String phone) {
        this.name = name;
        this.city = city;
        this.phone = phone;
    }

    public void update(String phone) {
        this.phone = phone;
    }
}
