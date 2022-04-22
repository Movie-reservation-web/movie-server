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
                .where(directorEq(condition.getDirector()), titleEq(condition.getTitle()),
                        actorEq(condition.getActor()))
                //.orderBy(new OrderSpecifier(Order.DESC, condition.getDirector()))
                .fetch();
        //releaseDateThan(condition.getReleaseDate()
    }

    private BooleanExpression directorEq(String directorCon) {
        return hasText(directorCon) ? movie.director.eq(directorCon) : null;
    }

    private BooleanExpression titleEq(String titleCon) {
        return titleCon != null ? movie.title.eq(titleCon) : null;
    }

    private BooleanExpression actorEq(String actorCon) {
        return actorCon != null ? movie.actors.contains(actorCon)  : null;
    }

    //상영중, 상영예정
//    private BooleanExpression releaseDateThan(LocalDate releaseDateCon) {
//        return releaseDateCon != null ? QMovie.movie.releaseDate. : null;
//    }

}