package study.movie.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.schedule.SeatEntity;

public interface SeatRepository extends JpaRepository<SeatEntity, Long> {


}