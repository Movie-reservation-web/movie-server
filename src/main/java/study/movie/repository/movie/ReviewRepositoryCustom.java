package study.movie.repository.movie;

import study.movie.domain.movie.Review;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<Review> findByMovieId(Long id);
}
