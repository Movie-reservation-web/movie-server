package study.movie.domain.theater.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.theater.dto.condition.TheaterSearchCond;
import study.movie.domain.theater.entity.CityCode;
import study.movie.domain.theater.entity.Theater;
import study.movie.global.utils.BasicRepositoryUtil;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;
import static study.movie.domain.theater.entity.QTheater.theater;


@Transactional
@Repository
@RequiredArgsConstructor
public class TheaterRepositoryImpl extends BasicRepositoryUtil implements TheaterRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Theater> findByCity(CityCode cityCode) {
        return queryFactory
                .selectFrom(theater)
                .where(cityEq(cityCode))
                .fetch();
    }

    @Override
    public Page<Theater> search(TheaterSearchCond cond, Pageable pageable) {
        List<Theater> elements = getSearchElementsQuery(cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(mapToOrderSpec(pageable.getSort(), Theater.class, theater))
                .fetch();

        JPAQuery<Long> countQuery = getSearchCountQuery(cond);

        return PageableExecutionUtils.getPage(
                elements,
                pageable,
                countQuery::fetchOne
        );
    }

    private JPAQuery<Theater> getSearchElementsQuery(TheaterSearchCond cond) {
        return queryFactory.selectFrom(theater)
                .where(
                        nameEq(cond.getName()),
                        cityEq(cond.getCity()),
                        phoneEq(cond.getPhone())
                );
    }

    private JPAQuery<Long> getSearchCountQuery(TheaterSearchCond cond) {
        return queryFactory.select(theater.count())
                .from(theater)
                .where(
                        nameEq(cond.getName()),
                        cityEq(cond.getCity()),
                        phoneEq(cond.getPhone())
                );
    }

    private BooleanExpression cityEq(CityCode cityCode) {
        return cityCode != null ? theater.city.eq(cityCode) : null;
    }

    private BooleanExpression nameEq(String name) {
        return hasText(name) ? theater.name.eq(name) : null;
    }

    private BooleanExpression phoneEq(String phone) {
        return hasText(phone) ? theater.phone.eq(phone) : null;
    }
}
