package study.movie.domain.theater.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.movie.domain.theater.dto.condition.ScreenSearchCond;
import study.movie.domain.theater.entity.Screen;

import java.util.Optional;

public interface ScreenRepositoryCustom {

    /**
     * 상영관 단건 조회
     * @param id
     */
    Optional<Screen> findScreenById(Long id);

    /**
     * 상영관 검색
     * @param cond screenFormat, theaterName
     * @param pageable
     * @return Page
     */
    Page<Screen> search(ScreenSearchCond cond, Pageable pageable);
}
