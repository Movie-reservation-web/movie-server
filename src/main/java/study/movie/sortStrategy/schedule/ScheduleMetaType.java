package study.movie.sortStrategy.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.metaModel.MetaModelType;

import static study.movie.domain.movie.QMovie.movie;
import static study.movie.domain.schedule.QSchedule.schedule;
import static study.movie.domain.theater.QTheater.theater;
import static study.movie.global.metaModel.MetaModelUtil.getColumn;

@Getter
@AllArgsConstructor
public enum ScheduleMetaType implements MetaModelType {
    MOVIE_TITLE("영화 제목", "movie-title", getColumn(movie.title)),
    SCREEN_DATE("상영 시간", "screen-date", getColumn(schedule.screenTime.startDateTime)),
    THEATER_NAME("극장 이름", "theater-name", getColumn(theater.name)),
    AUDIENCE("관객 수", "audience", getColumn(movie.audience));
    private String title;
    private String value;
    private String metaData;

    @Override
    public String getCode() {
        return title;
    }
}