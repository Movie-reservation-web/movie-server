package study.movie.repository.movie;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.movie.domain.movie.Review;

import java.util.List;

import static study.movie.domain.movie.QMovie.movie;
import static study.movie.domain.movie.QReview.review;

@Repository
@RequiredArgsConstructor
public class MoviequeryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Review> getReviews(Long id){
        return queryFactory.selectFrom(review)
                .leftJoin(review.movie, movie)
                .where(movie.id.eq(id))
                .fetch();

    }
}
