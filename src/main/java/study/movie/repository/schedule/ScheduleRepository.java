package study.movie.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.schedule.Schedule;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {

    Optional<Schedule> findByScheduleNumber(String scheduleNumber);
}
