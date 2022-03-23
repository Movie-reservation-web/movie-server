package study.movie.domain.theater;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.converter.theater.ScreenFormatConverter;
import study.movie.domain.schedule.Schedule;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Screen extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screen_id")
    private Long id;

    private String name;

    @Convert(converter = ScreenFormatConverter.class)
    private List<ScreenFormat> formats;

    private Integer maxRows;
    private Integer maxCols;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @JsonIgnore
    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>();

    //==생성 메서드==//
    @Builder
    public Screen(String name, List<ScreenFormat> formats, Integer maxRows, Integer maxCols, @NotNull Theater theater, List<Schedule> schedules) {
        this.name = name;
        this.formats = formats;
        this.maxRows = maxRows;
        this.maxCols = maxCols;
        this.theater = theater;
        this.schedules = schedules;
        this.registerTheater(theater);
    }

    //==연관 관계 메서드==//
    public void registerTheater(Theater theater) {
        this.theater = theater;
        theater.getScreens().add(this);
    }
}
