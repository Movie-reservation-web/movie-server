package study.movie.repository.movie;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.Movie;
import study.movie.domain.schedule.ScheduleStatus;

import javax.persistence.EntityManager;
import java.util.List;

import static study.movie.domain.movie.QMovie.movie;
import static study.movie.domain.schedule.QSchedule.schedule;
import static study.movie.domain.schedule.ScheduleStatus.OPEN;

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
    @Override
    public List<Movie> findMovieByOpenStatus() {
        return queryFactory.select(movie)
                .from(schedule)
                .leftJoin(schedule.movie, movie)
                .where(
                        scheduleStatusEq(OPEN)
                )
                .groupBy(movie.id)
                .orderBy(movie.audience.desc())
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

    private BooleanExpression scheduleStatusEq(ScheduleStatus status) {
        return status != null ? schedule.status.eq(status) : null;
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