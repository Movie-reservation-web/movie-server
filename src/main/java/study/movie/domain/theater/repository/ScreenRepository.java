package study.movie.domain.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.theater.Screen;

public interface ScreenRepository extends JpaRepository<Screen, Long> {
}
