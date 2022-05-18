package study.movie.repository.theater;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.Theater;

import java.util.Arrays;
import java.util.List;

public interface TheaterRepositoryCustom {

    List<Theater> findByCity(CityCode cityCode);
}
