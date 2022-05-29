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
import study.movie.domain.theater.dto.condition.ScreenSearchCond;
import study.movie.domain.theater.entity.Screen;
import study.movie.domain.theater.entity.ScreenFormat;
import study.movie.global.utils.BasicRepositoryUtil;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;
import static study.movie.domain.theater.entity.QScreen.screen;
import static study.movie.domain.theater.entity.QTheater.theater;

@Transactional
@Repository
@RequiredArgsConstructor
public class ScreenRepositoryImpl extends BasicRepositoryUtil implements ScreenRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Screen> search(ScreenSearchCond cond, Pageable pageable) {
        List<Screen> elements = getSearchElementsQuery(cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(mapToOrderSpec(pageable.getSort(), Screen.class, screen))
                .fetch();

        JPAQuery<Long> countQuery = getSearchCountQuery(cond);

        return PageableExecutionUtils.getPage(
                elements,
                pageable,
                countQuery::fetchOne
        );
    }

    private JPAQuery<Screen> getSearchElementsQuery(ScreenSearchCond cond) {
        return queryFactory.selectFrom(screen)
                .join(screen.theater, theater).fetchJoin()
                .where(
                        screenFormatEq(cond.getFormat()),
                        theaterNameEq(cond.getTheaterName())
                );
    }

    private JPAQuery<Long> getSearchCountQuery(ScreenSearchCond cond) {
        return queryFactory.select(theater.count())
                .from(theater)
                .leftJoin(screen.theater, theater)
                .where(
                        screenFormatEq(cond.getFormat()),
                        theaterNameEq(cond.getTheaterName())
                );
    }

    private BooleanExpression screenFormatEq(ScreenFormat screenFormat) {
        return screenFormat != null ? screen.format.eq(screenFormat) : null;
    }

    private BooleanExpression theaterNameEq(String theaterName) {
        return hasText(theaterName) ? theater.name.eq(theaterName) : null;
    }

}
