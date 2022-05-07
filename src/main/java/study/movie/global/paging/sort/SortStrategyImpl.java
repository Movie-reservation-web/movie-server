package study.movie.global.paging.sort;

import org.springframework.data.domain.Sort;
import study.movie.global.metaModel.MetaModelType;

public class SortStrategyImpl<T extends Enum<T> & MetaModelType> implements SortStrategy<T> {

    @Override
    public Sort.Order getSortOrder(T t, SortOption order) {
        return (order == SortOption.ASC) ? Sort.Order.asc(t.getMetaData()) : Sort.Order.desc(t.getMetaData());
    }
}
