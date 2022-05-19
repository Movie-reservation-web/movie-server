package study.movie.global.paging;

import lombok.*;
import study.movie.global.paging.sort.SortOption;
import study.movie.global.paging.sort.SortPair;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class PageableDTO {
    private Integer page;
    private Integer size;
    private List<SortPair<String, SortOption>> sorts;
}
