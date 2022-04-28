package study.movie.global.page;

import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class PageableDTO {
    private Integer page;
    private Integer size;
    private Integer totalElements;
    private List<SortPair<String, SortOption>> sorts;
}
