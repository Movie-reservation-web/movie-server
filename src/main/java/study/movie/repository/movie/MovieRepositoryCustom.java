package study.movie.repository.movie;

import study.movie.domain.movie.Movie;

import java.util.List;

public interface MovieRepositoryCustom {

    /**
     * 상영중인 영화 조회 (관객 수 내림차순 정렬)
     *
     * @return List
     */
    List<Movie> findMovieByOpenStatus();

    /**
     * 영화 관객 수 업데이트
     */
    void updateMovieAudience();
}
