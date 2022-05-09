package study.movie.repository.movie;

import study.movie.domain.movie.Movie;
import study.movie.dto.movie.MovieCondition;

import java.util.List;

public interface MovieRepositoryCustom {

    // 감독이나 배우로 영화를 찾을 때는 condition에 감독, 배우만 들어가면 됨
    List<Movie> findByCondition(MovieCondition condition);


    List<Movie> findByOrderBy(String orderCondition);
    List<Movie> findUnreleasedMovies();

    /**
     * 상영중인 영화 조회 (관객 수 내림차순 정렬)
     *
     * @return List
     */
    List<Movie> findMovieByOpenStatus();

}
