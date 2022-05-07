package study.movie.global.paging.sort;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 정렬할 컬럼명과 정렬 상태를 담는다.
 * @param <T>
 * @param <U>
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
@ToString
public class SortPair<T, U> {
    private T column;
    private U sortOption;

    public static <T, U> SortPair of(T column, U option) {
        return SortPair.builder()
                .column(column)
                .sortOption(option)
                .build();
    }
}
