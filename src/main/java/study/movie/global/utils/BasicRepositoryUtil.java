package study.movie.global.utils;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.Sort;
import study.movie.global.entity.BaseTimeEntity;

import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;

public class BasicRepositoryUtil {
    protected <T extends BaseTimeEntity, S extends EntityPathBase<T>> OrderSpecifier<?>[] mapToOrderSpec(Sort sort, Class<T> t, S s) {
        return sort.stream().map(
                order -> new OrderSpecifier(
                        order.getDirection().isAscending() ? ASC : DESC,
                        Expressions.path(t, s, order.getProperty()))
        ).toArray(OrderSpecifier[]::new);
    }

}
