package study.movie.theater.repository;

import com.querydsl.jpa.impl.JPAQuery;
import study.movie.theater.entity.Screen;

public interface ScreenRepositoryCustom {

    JPAQuery<Screen> findById(Long screenId);
}
