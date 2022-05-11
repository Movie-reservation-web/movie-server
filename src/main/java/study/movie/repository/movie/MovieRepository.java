package study.movie.repository.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.movie.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long>, MovieRepositoryCustom {

    List<Movie> findByDirector(String director);

//    List<Movie> findByActorsContains(String actor);
}
