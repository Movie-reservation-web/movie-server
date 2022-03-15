package study.movie.domain.screen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.screen.Screen;

public interface ScreenRepository extends JpaRepository<Screen, Long> {
}
