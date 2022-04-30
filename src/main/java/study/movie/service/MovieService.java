package study.movie.service;

import org.springframework.stereotype.Service;
import study.movie.dto.schedule.response.SimpleMovieResponse;

import java.util.List;

@Service
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
     */
    void updateMovieAudience();


}
