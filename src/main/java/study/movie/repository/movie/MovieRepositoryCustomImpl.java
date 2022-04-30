package study.movie.repository.movie;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.movie.domain.movie.Movie;
import study.movie.domain.schedule.ScheduleStatus;

import java.util.List;

import static study.movie.domain.movie.QMovie.movie;
import static study.movie.domain.schedule.QSchedule.schedule;
import static study.movie.domain.schedule.ScheduleStatus.OPEN;

@Repository
@RequiredArgsConstructor
public class MovieRepositoryCustomImpl implements MovieRepositoryCustom {
    private final JPAQueryFactory queryFactory;

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
}
