package study.movie.entitySearchStrategy.schedule;

import org.springframework.data.domain.Sort;
import study.movie.global.page.SortOption;
import study.movie.global.page.SortStrategy;
import study.movie.global.page.SortStrategyImpl;

public class ScheduleSortStrategy extends SortStrategyImpl<ScheduleSortType> implements SortStrategy<ScheduleSortType> {
    @Override
    public Sort.Order getSortOrder(ScheduleSortType domainKey, SortOption order) {
        switch (domainKey) {
            case ID_ASC:
            case ID_DESC:
            case AUDIENCE_ASC:
            case AUDIENCE_DESC:
                return super.getOrder(domainKey, order);
        }
        return null;
    }
}
