package study.movie.dto.schedule;

import lombok.Data;
import study.movie.converter.movie.FilmFormatConverter;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.FilmRating;
import study.movie.domain.schedule.Schedule;

import javax.persistence.Convert;
import java.time.LocalDate;
import java.util.List;

@Data
public class ReservationResponse {

    /**
     * 영화 정보
     */
    @Convert(converter = FilmFormatConverter.class)
    private FilmRating filmRating;
    private String title;
    @Convert(converter = FilmFormatConverter.class)
    private List<FilmFormat> filmFormats;

    /**
     * 극장 정보
     */
    private String theaterName;
    private String theaterCity;

    /**
     * 예매 날짜 정보
     */
    private LocalDate date;

    public ReservationResponse(Schedule schedule) {
        this.filmRating = schedule.getMovie().getFilmRating();
        this.title = schedule.getMovie().getTitle();
        this.filmFormats = schedule.getMovie().getFormats();
        this.theaterName = schedule.getScreen().getTheater().getName();
        this.theaterCity = String.valueOf(schedule.getScreen().getTheater().getCity());
        this.date = LocalDate.from(schedule.getStartTime());
    }
}
