package study.movie.repository.movie;

import study.movie.domain.movie.Movie;
import study.movie.dto.movie.MovieCondition;

import java.util.Arrays;
import java.util.List;

public interface MovieRepositoryCustom {
    List<Movie> findByCondition(MovieCondition condition);
}
