package study.movie.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.schedule.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
