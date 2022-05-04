package study.movie.repository.movie;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import study.movie.domain.movie.Review;

import java.util.List;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements  ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;
}
