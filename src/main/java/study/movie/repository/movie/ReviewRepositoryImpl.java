package study.movie.repository.movie;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import study.movie.domain.movie.Review;
import study.movie.dto.movie.condition.ReviewSearchCond;
import study.movie.global.utils.BasicRepositoryUtil;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;
import static study.movie.domain.movie.QMovie.movie;
import static study.movie.domain.movie.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl extends BasicRepositoryUtil implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Review> search(ReviewSearchCond cond, Pageable pageable) {
        List<Review> elements = getSearchElementsQuery(cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(mapToOrderSpec(pageable.getSort(), Review.class, review))
                .fetch();

        JPAQuery<Long> countQuery = getSearchCountQuery(cond);

        return PageableExecutionUtils.getPage(
                elements,
                pageable,
                countQuery::fetchOne);
    }

    private JPAQuery<Review> getSearchElementsQuery(ReviewSearchCond cond) {
        return queryFactory.selectFrom(review)
                .join(review.movie, movie).fetchJoin()
                .where(
                        writerEq(cond.getWriter()),
                        movieTitleEq(cond.getMovieTitle())
                );
    }

    private JPAQuery<Long> getSearchCountQuery(ReviewSearchCond cond) {
        return queryFactory.select(review.count())
                .from(review)
                .leftJoin(review.movie, movie)
                .where(
                        writerEq(cond.getWriter()),
                        movieTitleEq(cond.getMovieTitle())
                );
    }

    private BooleanExpression writerEq(String writer) {
        return hasText(writer) ? review.writer.eq(writer) : null;
    }

    private BooleanExpression movieTitleEq(String movieTitle) {
        return hasText(movieTitle) ? movie.title.eq(movieTitle) : null;
    }

}
