package study.movie.domain.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.movie.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
