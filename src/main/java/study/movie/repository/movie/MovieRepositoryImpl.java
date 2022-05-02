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

//    public List<Movie> findByCondition(MovieCondition condition) {
//        return queryFactory
//                .selectFrom(movie)
//                .where(directorEq(condition.getDirector()), titleEq(condition.getTitle()),
//                        actorEq(condition.getActor()))
//                //.orderBy(new OrderSpecifier(Order.DESC, condition.getDirector()))
//                .fetch();
//        //releaseDateThan(condition.getReleaseDate()
//    }
//
//    private BooleanExpression directorEq(String directorCon) {
//        return hasText(directorCon) ? movie.director.eq(directorCon) : null;
//    }
//
//    private BooleanExpression titleEq(String titleCon) {
//        return titleCon != null ? movie.title.eq(titleCon) : null;
//    }
//
//    private BooleanExpression actorEq(String actorCon) {
//        return actorCon != null ? movie.actors.contains(actorCon)  : null;
//    }

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

    @Override
    public void updateMovieAudience() {

    }

    private BooleanExpression scheduleStatusEq(ScheduleStatus status) {
        return status != null ? schedule.status.eq(status) : null;
    }

    //상영중, 상영예정
//    private BooleanExpression releaseDateThan(LocalDate releaseDateCon) {
//        return releaseDateCon != null ? QMovie.movie.releaseDate. : null;
//    }

}