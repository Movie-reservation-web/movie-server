package study.movie.global.page;

import org.springframework.data.domain.Sort;
import study.movie.global.metaModel.MetaModelType;

public class SortStrategyImpl<T extends Enum<T> & MetaModelType> {
    protected Sort.Order getOrder(T t, SortOption order) {
        return (order == SortOption.ASC) ? Sort.Order.asc(t.getMetaData()) : Sort.Order.desc(t.getMetaData());
    }
}
