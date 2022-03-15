package study.movie.domain.screen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.screen.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
