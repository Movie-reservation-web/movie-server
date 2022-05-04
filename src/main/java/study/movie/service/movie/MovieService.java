package study.movie.service.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.Review;
import study.movie.dto.movie.*;
import study.movie.repository.movie.MovieRepository;
import study.movie.repository.movie.ReviewRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    //saveMovie
    @Transactional
    public CreateMovieResponse saveMovie(CreateMovieRequest movieRequest){
        Movie createMovie = movieRequest.toEntity();
        movieRepository.save(createMovie);

        return new CreateMovieResponse(createMovie);
    }

    //saveReview
    @Transactional
    public CreateReviewResponse saveReview(CreateReviewRequest reviewRequest) throws Exception {
        Movie findMovie = movieRepository
                .findById(reviewRequest.getMovieId())
                .orElseThrow(() -> new Exception("noMovie"));
        //.orElseThrow(getExceptionSupplier());
        Review createReview = reviewRequest.toEntity(findMovie);
        reviewRepository.save(createReview);

        return new CreateReviewResponse(createReview);
    }

    //updateMovie
    @Transactional
    public void updateMovie(UpdateMovieRequest updateMovieRequest) throws Exception {
        //findMovie 생성 후
        Movie findMovie = movieRepository
                .findById(updateMovieRequest.getId())
                .orElseThrow(() -> new Exception("noMovie"));

        //findMovie.update(변경될 피드들만 파라미터)
        findMovie.update(updateMovieRequest.getFilmRating(),updateMovieRequest.getReleaseDate(),
                updateMovieRequest.getInfo(), updateMovieRequest.getImage());

    }

    //updateReview
    @Transactional
    public void updateReview(UpdateReviewRequest updateReviewRequest) throws Exception {
        Review findReview = reviewRepository
                .findById(updateReviewRequest.getId())
                .orElseThrow(() -> new Exception("noReview"));

        findReview.update(updateReviewRequest.getScore(),updateReviewRequest.getComment());
    }

    //deleteMovie
    @Transactional
    public void deleteMovie(Long movieId){
        movieRepository.deleteById(movieId);
    }

    //deleteReview
    @Transactional
    public void deleteReview(Long reviewId) throws Exception {
        Review deleteReview = reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new Exception("noReview"));

        //연관관계 끊으면 알아서 delete문 날림
        deleteReview.delete();
    }

    //findOneMovie
    public CreateMovieResponse findOneMovie(Long movieId) throws Exception {
        return new CreateMovieResponse(
                movieRepository
                .findById(movieId)
                .orElseThrow(() -> new Exception("noMovie")));
    }

    //findAllReview moiveid 가져와서 찾기 querydsl  생성
    public List<ReviewResponse> findAllReviewByMovieId(Long movieId){
        return reviewRepository.findByMovieId(movieId)
                .stream()
                .map(ReviewResponse :: new)
                .collect(Collectors.toList());
    }

    // By Director,Name,Actor,Date & orderBy Ratings,Score,Audience
    public List<CreateMovieResponse> findByCondition (MovieCondition condition){
        return movieRepository.findByCondition(condition)
                .stream()
                .map(CreateMovieResponse:: new)
                .collect(Collectors.toList());
    }
}
