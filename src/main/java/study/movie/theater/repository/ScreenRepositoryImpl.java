package study.movie.theater.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import study.movie.theater.entity.Screen;

import static study.movie.theater.entity.QScreen.screen;
import static study.movie.theater.entity.QTheater.theater;

@Transactional
@Repository
@RequiredArgsConstructor
public class ScreenRepositoryImpl implements ScreenRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public JPAQuery<Screen> findById(Long screenId) {
        return queryFactory.selectFrom(screen)
                .leftJoin(screen.theater, theater)
                .fetchJoin();
    }
}
