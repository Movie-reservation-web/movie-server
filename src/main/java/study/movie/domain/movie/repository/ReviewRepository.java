package study.movie.domain.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.movie.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
