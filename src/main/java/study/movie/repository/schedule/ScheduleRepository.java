package study.movie.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.movie.Movie;
import study.movie.domain.schedule.Schedule;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {

    List<Schedule> findByMovie(Movie movie);
}
