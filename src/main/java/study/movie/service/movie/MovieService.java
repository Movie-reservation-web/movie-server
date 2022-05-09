package study.movie.service.movie;

import study.movie.dto.movie.*;
import study.movie.dto.schedule.response.SimpleMovieResponse;

import java.util.List;

public interface MovieService {


    /**
     * Api Server
     * <p>
     * 상영중인 영화 차트 조회
     */
    List<SimpleMovieResponse> findAllOpenMovies();

    /**
     * Batch Server
     * <p>
     * 영화 관람객 수 증가
     * 하루 단위로 메서드 실행
     */
    void updateMovieAudience();

    //saveMovie
    BasicMovieResponse saveMovie(CreateMovieRequest movieRequest);

    //saveReview
    Long saveReview(CreateReviewRequest reviewRequest);

    //updateMovie
    void updateMovie(UpdateMovieRequest updateMovieRequest);

    //updateReview
    void updateReview(UpdateReviewRequest updateReviewRequest);

    //deleteMovie
    void deleteMovie(Long movieId);

    //deleteReview
    void deleteReview(Long reviewId);

    //findOneMovie
    BasicMovieResponse findOneMovie(Long movieId);

    //findAllReview moiveid 가져와서 찾기 querydsl  생성
    List<ReviewResponse> findAllReviewByMovieId(Long movieId);

    // By Director,Name,Actor & orderBy Date Default
    List<FindMovieResponse> findByCondition(MovieCondition condition);

    //상영예정작보기
    List<FindMovieResponse> findUnreleasedMovies();

    //영화 차트 보기_orderBy Ratings,Score,Audience
    List<FindMovieResponse> findByOrderBy(String orderCondition);


}
