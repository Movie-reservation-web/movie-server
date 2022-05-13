package study.movie.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.theater.entity.Screen;

public interface ScreenRepository extends JpaRepository<Screen, Long> {
}
