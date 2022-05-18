package study.movie.repository.theater;

import com.querydsl.jpa.impl.JPAQuery;
import study.movie.domain.theater.Screen;

public interface ScreenRepositoryCustom {

    JPAQuery<Screen> findById(Long screenId);
}
