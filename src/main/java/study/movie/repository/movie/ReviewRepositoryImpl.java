package study.movie.repository.movie;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import study.movie.domain.movie.Review;

import java.util.List;

import static study.movie.domain.movie.QMovie.movie;
import static study.movie.domain.movie.QReview.review;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements  ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Review> findByMovieId(Long id) {
        return queryFactory.selectFrom(review)
                .join(review.movie, movie)
                .where(movie.id.eq(id))
                .fetch();
    }
}
