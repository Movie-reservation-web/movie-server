package study.movie.dto.schedule;

import lombok.Data;
import study.movie.domain.schedule.Schedule;

import java.time.LocalDate;

@Data
public class ScheduleSearchResponse extends BaseScheduleResponse{

    private ScheduleMovieResponse movie;
    private LocalDate screenDate;
    private ScheduleTheaterResponse theater;

    public ScheduleSearchResponse(Schedule schedule) {
        this.id = schedule.getId();
        this.movie = new ScheduleMovieResponse(schedule.getMovie());
        this.screenDate = schedule.getStartTime().toLocalDate();
        this.theater = new ScheduleTheaterResponse(schedule.getScreen().getTheater());
    }
}
