package study.movie.domain.theater.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.movie.domain.theater.dto.condition.ScreenSearchCond;
import study.movie.domain.theater.entity.Screen;

public interface ScreenRepositoryCustom {

    /**
     * 상영관 검색
     * @param cond screenFormat, theaterName
     * @param pageable
     * @return Page
     */
    Page<Screen> search(ScreenSearchCond cond, Pageable pageable);
}
