package study.movie.domain.movie.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import study.movie.domain.movie.dto.condition.MovieChartSortType;
import study.movie.domain.movie.dto.condition.MovieSearchCond;
import study.movie.domain.movie.entity.FilmRating;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.schedule.entity.ScheduleStatus;
import study.movie.global.utils.BasicRepositoryUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;
import static study.movie.domain.movie.entity.QMovie.movie;
import static study.movie.domain.movie.entity.QReview.review;
import static study.movie.domain.schedule.entity.QSchedule.schedule;
import static study.movie.domain.schedule.entity.ScheduleStatus.OPEN;

@Repository
@RequiredArgsConstructor
public class MovieRepositoryImpl extends BasicRepositoryUtil implements MovieRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Movie> findMovieBySort(MovieChartSortType sortType, boolean isReleased) {
        return queryFactory.select(movie)
                .from(schedule)
                .leftJoin(schedule.movie, movie)
                .where(
                        scheduleStatusEq(OPEN),
                        isMovieReleased(isReleased)
                )
                .groupBy(movie.id)
                .orderBy(movieCharOrder(sortType))
                .fetch();
    }

    @Override
    public List<Movie> findUnreleasedMovies() {
        return queryFactory
                .selectFrom(movie)
                .where(movie.releaseDate.after(LocalDate.now()))
                .orderBy(movie.releaseDate.asc())
                .fetch();
    }

    @Override
    public List<Movie> findUpdatedAudienceMovies() {
        List<Tuple> fetch = queryFactory.select(schedule.reservedSeatCount.sum().longValue(), movie)
                .from(schedule)
                .leftJoin(schedule.movie, movie)
                .where(scheduleStatusEq(OPEN)
                )
                .groupBy(movie)
                .having(schedule.reservedSeatCount.sum().longValue().ne(movie.audience))
                .fetch();

        for (Tuple tuple : fetch) {
            Movie movie = tuple.get(1, Movie.class);
            Long count = tuple.get(0, Long.class);
            if (movie != null && count != null) movie.updateAudience(count);

        }
        return fetch.stream()
                .map(tuple -> Objects.requireNonNull(tuple.get(1, Movie.class)))
                .collect(Collectors.toList());
    }

    @Override
    public Page<Movie> search(MovieSearchCond cond, Pageable pageable) {
        List<Movie> elements = getSearchElementsQuery(cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(mapToOrderSpec(pageable.getSort(), Movie.class, movie))
                .fetch();

        JPAQuery<Long> countQuery = getSearchCountQuery(cond);

        return PageableExecutionUtils.getPage(
                elements,
                pageable,
                countQuery::fetchOne);

    }

    private JPAQuery<Movie> getSearchElementsQuery(MovieSearchCond cond) {
        return queryFactory.selectFrom(movie)
                .where(
                        movieTitleEq(cond.getTitle()),
                        movieDirectorEq(cond.getDirector()),
                        movieNationEq(cond.getNation()),
                        movieFilmRatingEq(cond.getFilmRating())
                );
    }

    private JPAQuery<Long> getSearchCountQuery(MovieSearchCond cond) {
        return queryFactory.select(review.count())
                .from(review)
                .where(
                        movieTitleEq(cond.getTitle()),
                        movieDirectorEq(cond.getDirector()),
                        movieNationEq(cond.getNation()),
                        movieFilmRatingEq(cond.getFilmRating())
                );
    }

    private BooleanExpression movieTitleEq(String title) {
        return hasText(title) ? movie.title.eq(title) : null;
    }

    private BooleanExpression movieDirectorEq(String director) {
        return hasText(director) ? movie.director.eq(director) : null;
    }

    private BooleanExpression movieNationEq(String nation) {
        return hasText(nation) ? movie.nation.eq(nation) : null;
    }

    private BooleanExpression movieFilmRatingEq(FilmRating filmRating) {
        return filmRating != null ? movie.filmRating.eq(filmRating) : null;
    }

    private BooleanExpression isMovieReleased(boolean isReleased) {
        return isReleased ? movie.releaseDate.before(LocalDate.now()) : null;
    }

    private BooleanExpression scheduleStatusEq(ScheduleStatus status) {
        return status != null ? schedule.status.eq(status) : null;
    }

    private OrderSpecifier<?> movieCharOrder(MovieChartSortType sortType) {
        return new OrderSpecifier(Order.DESC, Expressions.path(Movie.class, movie, sortType.getMetaData()));
    }
}
