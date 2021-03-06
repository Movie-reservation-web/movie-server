package study.movie.domain.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.theater.entity.Theater;

public interface TheaterRepository extends JpaRepository<Theater, Long>, TheaterRepositoryCustom {

}
