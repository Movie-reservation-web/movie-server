package study.movie.init;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.dto.request.CreateMovieRequest;
import study.movie.domain.movie.dto.request.CreateReviewRequest;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.movie.entity.Review;
import study.movie.domain.movie.repository.MovieRepository;
import study.movie.global.utils.JsonUtil;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InitMovieService {
    private static final String MOVIE = "movie";
    private static final String REVIEW = "review";
    private final MovieRepository movieRepository;

    public void initMovieData() {
        movieRepository.saveAll(
                JsonUtil.jsonArrayToList(MOVIE, CreateMovieRequest.class).stream()
                        .map(CreateMovieRequest::toEntity)
                        .collect(Collectors.toList()
                        )
        );
    }

    @Transactional
    public void initReviewData() {
        JsonUtil.jsonArrayToList(REVIEW, CreateReviewRequest.class).stream()
                .map(this::mapToReview)
                .collect(Collectors.toList()
                );
    }

    private Review mapToReview(CreateReviewRequest request) {
        Movie findMovie = movieRepository.findAll().get((int) (request.getMovieId() - 1));
        Review review = Review.writeReview()
                .movie(findMovie)
                .writer(request.getWriter())
                .score(request.getScore())
                .comment(request.getComment())
                .build();
        return review;
    }
}
