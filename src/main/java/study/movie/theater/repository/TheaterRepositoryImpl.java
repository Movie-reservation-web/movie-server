package study.movie.theater.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import study.movie.theater.entity.CityCode;
import study.movie.theater.entity.Theater;

import java.util.List;

import static study.movie.theater.entity.QTheater.theater;

@Transactional
@Repository
@RequiredArgsConstructor
public class TheaterRepositoryImpl implements TheaterRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Theater> findByCity(CityCode cityCode) {
        return queryFactory
                .selectFrom(theater)
                .where(theater.city.eq(cityCode))
                .fetch();
    }
}
