package study.movie.theater.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.movie.entity.FilmFormat;
import study.movie.schedule.entity.Schedule;
import study.movie.global.entity.BaseTimeEntity;
import study.movie.exception.CustomException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static study.movie.exception.ErrorCode.NOT_ALLOW_SCREEN_FORMAT;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Screen extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screen_id")
    private Long id;

    private String name;

    private ScreenFormat format;

    private Integer maxRows;
    private Integer maxCols;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @JsonIgnore
    @OneToMany(mappedBy = "screen")
    private List<Schedule> schedules = new ArrayList<>();

    //==생성 메서드==//
    @Builder
    public Screen(String name, ScreenFormat format, Integer maxRows, Integer maxCols, Theater theater) {
        this.format = format;
        this.name = name;
        this.maxRows = maxRows;
        this.maxCols = maxCols;
        registerTheater(theater);
    }

    //==연관 관계 메서드==//
    public void registerTheater(Theater theater) {
        this.theater = theater;
        theater.getScreens().add(this);
    }

    //==비즈니스 로직==//
    public void checkAllowedMovieFormat(List<FilmFormat> filmFormats){
        if (!filmFormats.stream().allMatch(value -> format.isAllowedFilmFormat(value)))
            throw new CustomException(NOT_ALLOW_SCREEN_FORMAT);
    }

    public void update(ScreenFormat format, Integer maxRows, Integer maxCols) {
        this.updateFormat(format);
        this.updateMaxRows(maxRows);
        this.updateMaxCols(maxCols);
    }

    private void updateFormat(ScreenFormat format) {
        this.format = format;
    }
    private void updateMaxRows(Integer maxRows) {
        this.maxRows = maxRows;
    }
    private void updateMaxCols( Integer maxCols) {
        this.maxCols = maxCols;
    }
}
