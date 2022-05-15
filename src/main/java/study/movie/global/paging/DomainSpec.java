package study.movie.global.paging;

import com.mysema.commons.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import study.movie.exception.CustomException;
import study.movie.global.paging.sort.SortOption;
import study.movie.global.paging.sort.SortPair;
import study.movie.global.paging.sort.SortStrategy;
import study.movie.global.paging.sort.SortStrategyImpl;

import java.util.ArrayList;
import java.util.List;

import static study.movie.exception.ErrorCode.INVALID_SORT_OPTION;

@AllArgsConstructor
public class DomainSpec<T extends Enum<T>> {
    private final Class<T> clazz;
    @Getter
    @Setter
    private int defaultPage = 0;
    @Getter
    @Setter
    private int defaultSize = 20;
    private SortStrategy<T> sortStrategy;

    public DomainSpec(Class<T> clazz) {
        this.clazz = clazz;
        this.sortStrategy = new SortStrategyImpl();
    }

    public void changeSortStrategy(SortStrategy<T> sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    public Pageable getPageable(PageableDTO dto) {
        Integer page = dto.getPage();
        Integer size = dto.getSize();
        return dto.getSorts().isEmpty() ?
                PageRequest.of(
                        page == null ? defaultPage : page,
                        size == null ? defaultSize : size
                ) :
                PageRequest.of(
                        page == null ? defaultPage : page,
                        size == null ? defaultSize : size,
                        Sort.by(getSortOrders(dto.getSorts()))
                );
    }

    private List<Sort.Order> getSortOrders(List<SortPair<String, SortOption>> sorts) {
        Assert.notNull(this.sortStrategy, "There is no sort strategy");

        List<Sort.Order> orders = new ArrayList<>();
        for (var o : sorts) {
            T column;
            try {

                column = Enum.valueOf(this.clazz, o.getColumn());

            } catch (IllegalArgumentException e) {
                throw new CustomException(e, INVALID_SORT_OPTION);
            }
            final Sort.Order order = sortStrategy.getSortOrder(column, o.getSortOption());
            Assert.notNull(order, "sort option error");

            orders.add(order);
        }
        return orders;
    }
}
