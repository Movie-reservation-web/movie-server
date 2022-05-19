package study.movie.theater.repository;

import study.movie.theater.entity.CityCode;
import study.movie.theater.entity.Theater;

import java.util.List;

public interface TheaterRepositoryCustom {

    List<Theater> findByCity(CityCode cityCode);
}
