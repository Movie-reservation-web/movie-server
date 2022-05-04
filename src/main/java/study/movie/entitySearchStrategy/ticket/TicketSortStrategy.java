package study.movie.entitySearchStrategy.ticket;

import org.springframework.data.domain.Sort;
import study.movie.global.page.SortOption;
import study.movie.global.page.SortStrategy;
import study.movie.global.page.SortStrategyImpl;

public class TicketSortStrategy extends SortStrategyImpl<TicketSortType> implements SortStrategy<TicketSortType> {
    @Override
    public Sort.Order getSortOrder(TicketSortType domainKey, SortOption order) {
        switch (domainKey) {
            case ID_ASC:
            case ID_DESC:
            case PRICE_ASC:
            case PRICE_DESC:
                return super.getOrder(domainKey, order);
        }
        return null;
    }
}
