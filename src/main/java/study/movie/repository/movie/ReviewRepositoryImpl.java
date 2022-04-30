package study.movie.repository.movie;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements  ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;
}
