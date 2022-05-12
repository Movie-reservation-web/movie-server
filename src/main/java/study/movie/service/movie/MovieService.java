package study.movie.service.movie;

import org.springframework.data.domain.Page;
import study.movie.dto.movie.condition.MovieSearchCond;
import study.movie.dto.movie.condition.MovieSearchType;
import study.movie.dto.movie.condition.MovieChartSortType;
import study.movie.dto.movie.request.CreateMovieRequest;
import study.movie.dto.movie.request.UpdateMovieRequest;
import study.movie.dto.movie.response.BasicMovieResponse;
import study.movie.dto.movie.response.FindMovieResponse;
import study.movie.dto.movie.response.MovieSearchResponse;
import study.movie.dto.schedule.response.MovieChartResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.PageableDTO;

import java.util.List;

public interface MovieService {

    /**
     * Api Server
     * <p>
     * 영화 차트 조회
     * 관람객, 평점 순으로 정렬, 영화의 개봉여부
     */
    List<MovieChartResponse> findMovieBySort(MovieChartSortType sortType, boolean isReleased);

    /**
     * Api Server
     * <p>
     * 상영 예정작 조회
     */
    List<MovieChartResponse> findUnreleasedMovies();

    /**
     * Api Server
     * <p>
     * 선택한 영화 조회, 해당 영화의 리뷰까지 조회
     */
    BasicMovieResponse findOneMovie(Long movieId);

    /**
     * Api Server
     * <p>
     * 영화 조회(배우로 검색)
     */
    List<FindMovieResponse> findMovieByActor(String actor);

    /**
     * Api Server
     * <p>
     * 영화 조회(배우로 검색)
     */
    List<FindMovieResponse> findMovieByDirector(String director);


    /**
     * Admin Server
     * <p>
     * 영화 저장
     */
    PostIdResponse save(CreateMovieRequest request);

    /**
     * Admin Server
     * <p>
     * 영화 수정
     */
    void update(Long id, UpdateMovieRequest request);

    /**
     * Admin Server
     * <p>
     * 영화 삭제
     */
    void delete(Long movieId);

    /**
     * Admin Server
     * <p>
     * 모든 영화 조회
     */
    Page<MovieSearchResponse> search(MovieSearchType type, MovieSearchCond cond, PageableDTO pageableDTO);


    /**
     * Batch Server
     * <p>
     * 영화 관람객 수 증가
     * 하루 단위로 메서드 실행
     */
    void updateMovieAudience();

}
