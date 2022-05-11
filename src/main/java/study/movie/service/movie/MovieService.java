package study.movie.service.movie;

import study.movie.dto.movie.*;
import study.movie.dto.schedule.response.MovieChartResponse;
import study.movie.global.dto.PostIdResponse;

import java.util.List;

public interface MovieService {

    /**
     * Batch Server
     * <p>
     * 영화 관람객 수 증가
     * 하루 단위로 메서드 실행
     */
    void updateMovieAudience();

    //saveMovie
    PostIdResponse saveMovie(CreateMovieRequest request);


    //updateMovie
    void updateMovie(UpdateMovieRequest request);

    //deleteMovie
    void deleteMovie(Long movieId);

    //findOneMovie
    BasicMovieResponse findOneMovie(Long movieId);

    // By Director,Name,Actor & orderBy Date Default
    List<FindMovieResponse> findMovieByActor(String actor);

    List<FindMovieResponse> findMovieByDirector(String director);

    //상영예정작보기
    List<MovieChartResponse> findUnreleasedMovies();

    //영화 차트 보기_orderBy Ratings,Score,Audience
    List<MovieChartResponse> findMovieBySort(MovieSortType sortType, boolean isReleased);


}
