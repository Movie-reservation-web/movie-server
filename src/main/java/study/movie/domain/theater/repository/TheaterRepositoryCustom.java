package study.movie.domain.theater.repository;

import study.movie.domain.theater.entity.CityCode;
import study.movie.domain.theater.entity.Theater;

import java.util.List;

public interface TheaterRepositoryCustom {

    List<Theater> findByCity(CityCode cityCode);
}
