package study.movie.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.schedule.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {

}
