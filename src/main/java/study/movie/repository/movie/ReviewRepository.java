package study.movie.repository.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.movie.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    List<Review> findByMovieId(Long movieId);
}
