package study.movie.service.movie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.Review;
import study.movie.domain.ticket.Ticket;
import study.movie.dto.movie.*;
import study.movie.dto.schedule.response.SimpleMovieResponse;
import study.movie.global.utils.BasicServiceUtils;
import study.movie.repository.movie.MovieRepository;
import study.movie.repository.movie.ReviewRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;
import static study.movie.global.exception.ErrorCode.MOVIE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MovieServiceImpl extends BasicServiceUtils implements MovieService {
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;



    @Override
    public List<SimpleMovieResponse> findAllOpenMovies() {
        List<Movie> movies = movieRepository.findMovieByOpenStatus();
        double totalCount = movies.stream().mapToDouble(Movie::getAudience).sum();
        return movies.stream()
                .map(movie -> SimpleMovieResponse.of(movie, totalCount))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateMovieAudience() {
        List<Movie> movies = movieRepository.findMovieByOpenStatus();
        for (Movie movie : movies) {
            movie.addAudience(
                    movie.getTickets().stream().mapToInt(Ticket::getReservedMemberCount).sum());
        }
    }

    @Transactional
    public BasicMovieResponse saveMovie(CreateMovieRequest movieRequest) {
        Movie createMovie = movieRequest.toEntity();
        movieRepository.save(createMovie);

        return new BasicMovieResponse(createMovie);
    }

    //saveReview
    @Transactional
    public Long saveReview(CreateReviewRequest reviewRequest) {
        Movie findMovie = movieRepository
                .findById(reviewRequest.getMovieId())
                .orElseThrow(getExceptionSupplier(MOVIE_NOT_FOUND));

        Review review = Review.builder()
                .movie(findMovie)
                .writer(reviewRequest.getWriter())
                .score(reviewRequest.getScore())
                .comment(reviewRequest.getComment())
                .build();

        return review.getId();
    }

    //updateMovie
    @Transactional
    public void updateMovie(UpdateMovieRequest updateMovieRequest) {
        //findMovie 생성 후
        Movie findMovie = movieRepository
                .findById(updateMovieRequest.getId())
                .orElseThrow(() -> new RuntimeException("noMovie"));

        //findMovie.update(변경될 피드들만 파라미터)
        findMovie.update(updateMovieRequest.getFilmRating(), updateMovieRequest.getReleaseDate(),
                updateMovieRequest.getInfo(), updateMovieRequest.getImage());

    }

    //updateReview
    @Transactional
    public void updateReview(UpdateReviewRequest updateReviewRequest) {
        Review findReview = reviewRepository
                .findById(updateReviewRequest.getId())
                .orElseThrow(() -> new RuntimeException("noReview"));
        findReview.update(updateReviewRequest.getScore(), updateReviewRequest.getComment());
    }

    //deleteMovie
    @Transactional
    public void deleteMovie(Long movieId) {
        movieRepository.deleteById(movieId);
    }

    //deleteReview
    @Transactional
    public void deleteReview(Long reviewId) {
        Review deleteReview = reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new RuntimeException("noReview"));

        //연관관계 끊으면 알아서 delete문 날림
        deleteReview.delete();

//        reviewRepository.deleteByIdEqQuery(reviewId);
    }

    //findOneMovie
    public BasicMovieResponse findOneMovie(Long movieId) {
        return new BasicMovieResponse(
                movieRepository
                        .findById(movieId)
                        .orElseThrow(() -> new RuntimeException("noMovie")));
    }

    //findAllReview moiveid 가져와서 찾기 querydsl  생성
    public List<ReviewResponse> findAllReviewByMovieId(Long movieId) {
       Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("noMovie"));

        List<Review> reviewList = movie.getReviews();


//        return reviewRepository.findByMovieId(movieId)
//                .stream()
//                .map(ReviewResponse::new)
//                .collect(Collectors.toList());
        return null;
    }

    // By Director,Name,Actor & orderBy Date Default
    public List<FindMovieResponse> findByCondition(MovieCondition condition) {
        return movieRepository.findByCondition(condition)
                .stream()
                .filter(movie -> hasActor(condition.getActor(), movie.getActors()))
                .map(FindMovieResponse::new)
                .collect(Collectors.toList());
    }

    private boolean hasActor(String condition, List<String> actors) {
        return !hasText(condition) || actors.contains(condition);
    }

    //상영예정작보기
    public List<FindMovieResponse> findUnreleasedMovies() {
        return movieRepository.findUnreleasedMovies()
                .stream()
                .map(FindMovieResponse::new)
                .collect(Collectors.toList());
    }

    //영화 차트 보기_orderBy Ratings,Score,Audience
    public List<FindMovieResponse> findByOrderBy(String orderCondition) {
        return movieRepository.findByOrderBy(orderCondition)
                .stream()
                .map(FindMovieResponse::new)
                .collect(Collectors.toList());
    }
}
