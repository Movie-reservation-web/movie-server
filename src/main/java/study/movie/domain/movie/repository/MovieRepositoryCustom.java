package study.movie.domain.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.movie.dto.condition.MovieChartSortType;
import study.movie.domain.movie.dto.condition.MovieSearchCond;

import java.util.List;

public interface MovieRepositoryCustom {

    /**
     * 영화 조회(정렬, 상영 여부)
     * @param sortType audience/desc, avg-score/desc
     * @param isReleased
     * @return
     */
    List<Movie> findMovieBySort(MovieChartSortType sortType, boolean isReleased);

    /**
     * 미개봉 영화 조회
     * @return List
     */
    List<Movie> findUnreleasedMovies();

    /**
     * 상영중인 영화 조회
     *
     * @return List
     */
    List<Movie> findUpdatedAudienceMovies();

    /**
     * 영화 검색
     * @param cond title, director, nation, filmRating
     * @param pageable id(asc,desc), avg-score(desc), audience(desc), running-time(asc,desc)
     * @return Page
     */
    Page<Movie> search(MovieSearchCond cond, Pageable pageable);
}
