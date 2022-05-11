package study.movie.repository.movie;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.movie.domain.movie.Movie;
import study.movie.domain.schedule.ScheduleStatus;
import study.movie.dto.movie.MovieSortType;

import java.time.LocalDate;
import java.util.List;

import static study.movie.domain.movie.QMovie.movie;
import static study.movie.domain.schedule.QSchedule.schedule;
import static study.movie.domain.schedule.ScheduleStatus.OPEN;

@Repository
@RequiredArgsConstructor
public class MovieRepositoryImpl implements MovieRepositoryCustom {

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

    //영화 차트 보기_orderBy Ratings,Score,Audience
    @Override
    public List<Movie> findMovieBySort(MovieSortType sortType, boolean isReleased) {
        return queryFactory.select(movie)
                .from(schedule)
                .leftJoin(schedule.movie, movie)
                .where(
                        scheduleStatusEq(OPEN),
                        isMovieReleased(isReleased)
                )
                .groupBy(movie.id)
                .orderBy(movieSort(sortType))
                .fetch();
    }

    private BooleanExpression isMovieReleased(boolean isReleased) {
        return isReleased ? movie.releaseDate.before(LocalDate.now()) : null;
    }

    @Override
    public List<Movie> findUnreleasedMovies() {
        return queryFactory
                .selectFrom(movie)
                .where(movie.releaseDate.after(LocalDate.now()))
                .orderBy(movie.releaseDate.asc())
                .fetch();
    }

    private BooleanExpression scheduleStatusEq(ScheduleStatus status) {
        return status != null ? schedule.status.eq(status) : null;
    }

    private OrderSpecifier<?> movieSort(MovieSortType sortType) {
        return new OrderSpecifier(Order.DESC, Expressions.path(Movie.class, movie, sortType.getMetaData()));
    }
}