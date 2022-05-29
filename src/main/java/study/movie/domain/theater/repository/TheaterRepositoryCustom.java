package study.movie.domain.theater.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.movie.domain.theater.dto.condition.TheaterSearchCond;
import study.movie.domain.theater.entity.CityCode;
import study.movie.domain.theater.entity.Theater;

import java.util.List;

public interface TheaterRepositoryCustom {

    List<Theater> findByCity(CityCode cityCode);

    /**
     * 영화관 조회
     *
     * @param cond     name, city, phone
     * @param pageable
     * @return Page
     */
    Page<Theater> search(TheaterSearchCond cond, Pageable pageable);
}
