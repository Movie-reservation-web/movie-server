package study.movie.repository.movie;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.QMovie;
import study.movie.dto.movie.MovieCondition;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.springframework.util.StringUtils.*;
import static study.movie.domain.movie.QMovie.*;

@Transactional
@Repository
@RequiredArgsConstructor
public class MovieRepositoryImpl implements MovieRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public List<Movie> findByCondition(MovieCondition condition) {
        return queryFactory
                .selectFrom(movie)
                .where(directorEq(condition.getDirector()),
                        titleEq(condition.getTitle()))
                .orderBy(movie.releaseDate.desc())
                .fetch();
    }

    private BooleanExpression directorEq(String directorCon) {
        return hasText(directorCon) ? movie.director.eq(directorCon) : null;
    }

    private BooleanExpression titleEq(String titleCon) {
        return hasText(titleCon) ? movie.title.eq(titleCon) : null;
    }

    //영화 차트 보기_orderBy Ratings,Score,Audience
    @Override
    public List<Movie> findByOrderBy(String orderCondition) {
        return queryFactory
                .selectFrom(movie)
                .where(movie.releaseDate.before(LocalDate.now()))
                .orderBy(orderExpress(orderCondition))
                .fetch();
    }

    private OrderSpecifier<String> orderExpress(String orderCondition){
        switch (orderCondition) {
//            case "Ratings":
//                return new OrderSpecifier(Order.DESC,);
//            case "Score":
//                return new OrderSpecifier(Order.DESC, movie.score);
            case "Audience":
                return new OrderSpecifier(Order.DESC, movie.audience);
        }
        return null;
    }

    @Override
    public List<Movie> findUnreleasedMovies() {
        return queryFactory
                .selectFrom(movie)
                .where(movie.releaseDate.after(LocalDate.now()))
                .orderBy(movie.releaseDate.desc())
                .fetch();
    }
}