package study.movie.sortStrategy.schedule;

import org.springframework.data.domain.Sort;
import study.movie.global.page.SortOption;
import study.movie.global.page.SortStrategy;
import study.movie.global.page.SortStrategyImpl;

public class ScheduleSortStrategy extends SortStrategyImpl<ScheduleMetaType> implements SortStrategy<ScheduleMetaType> {
    @Override
    public Sort.Order getSortOrder(ScheduleMetaType domainKey, SortOption order) {
        switch (domainKey) {
            case MOVIE_TITLE:
            case THEATER_NAME:
            case SCREEN_DATE:
            case AUDIENCE:
                return super.getOrder(domainKey, order);
        }
        return null;
    }
}
