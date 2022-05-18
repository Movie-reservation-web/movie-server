package study.movie.repository.theater;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.Theater;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;
import static study.movie.domain.movie.QMovie.movie;
import static study.movie.domain.theater.QTheater.theater;

@Transactional
@Repository
@RequiredArgsConstructor
public class TheaterRepositoryImpl implements TheaterRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Theater> findByCity(CityCode cityCode) {
        return queryFactory
                .selectFrom(theater)
                .where(theater.city.eq(cityCode))
                .fetch();
    }
}
