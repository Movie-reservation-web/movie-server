package study.movie.init;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.movie.domain.movie.dto.request.CreateMovieRequest;
import study.movie.domain.movie.repository.MovieRepository;
import study.movie.global.utils.JsonUtil;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InitMovieService {
    private static final String MOVIE = "movie";
    private final MovieRepository movieRepository;

    public void initMovieDate() {
        movieRepository.saveAll(
                JsonUtil.jsonArrayToList(MOVIE, CreateMovieRequest.class).stream()
                        .map(CreateMovieRequest::toEntity)
                        .collect(Collectors.toList()
                        )
        );
    }
}
