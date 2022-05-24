package study.movie.domain.theater.repository;

import com.querydsl.jpa.impl.JPAQuery;
import study.movie.domain.theater.entity.Screen;

public interface ScreenRepositoryCustom {

    JPAQuery<Screen> findByScreenId(Long screenId);
}
