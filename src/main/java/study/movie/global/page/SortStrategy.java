package study.movie.global.page;

import org.springframework.data.domain.Sort;

public interface SortStrategy<T extends Enum<T>> {
    Sort.Order getSortOrder(T domainKey, SortOption order);
}
