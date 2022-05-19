package study.movie.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.theater.entity.Theater;

public interface TheaterRepository extends JpaRepository<Theater, Long>, TheaterRepositoryCustom {

}
