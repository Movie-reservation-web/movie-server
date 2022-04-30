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
    public MovieResponse saveMovie(MovieRequest movieRequest){
        Movie createMovie = Movie.builder()
                .title(movieRequest.getTitle())
                .runningTime(movieRequest.getRunningTime())
                .filmRating(movieRequest.getFilmRating())
                .director(movieRequest.getDirector())
                .actors(movieRequest.getActors())
                .genres(movieRequest.getGenres())
                .formats(movieRequest.getFormats())
                .nation(movieRequest.getNation())
                .releaseDate(movieRequest.getReleaseDate())
                .info(movieRequest.getInfo())
                .image(movieRequest.getImage())
                .build();

        movieRepository.save(createMovie);

        return new MovieResponse(createMovie);
    }

    //saveReview
//    @Transactional
//    public ReviewResponse saveReview(ReviewRequest reviewRequest) throws Exception {
//        Movie findMovie = movieRepository
//                .findById(reviewRequest.getMovieId())
//                .orElseThrow(() -> new Exception("noMovie"));
//        //.orElseThrow(getExceptionSupplier());
//
////        Review createReview = Review.builder()
////                .movie(findMovie)
////                .writer(reviewRequest.getWriter())
////                .score(reviewRequest.getScore())
////                .comment(reviewRequest.getComment())
////                .build();
//
//        return new ReviewResponse(createReview);
 //   }

    //updateMovie
    @Transactional
    public void updateMovie(UpdateMovieRequest updateMovieRequest){
        //findMovie 생성 후
        //findMovie.update(변경될 피드들만 파라미터)
    }

    //updateReview
    @Transactional
    public void updateReview(){

    }

    //deleteMovie
    @Transactional
    public void deleteMovie(Long movieId){
        movieRepository.deleteById(movieId);
    }

    //deleteReview
    @Transactional
    public void deleteReview(Long id) throws Exception {
//        Review deleteReview = find
//        deleteReview.delete();
        //연관관계 끊으면 알아서 delete문 날림
    }

    //findOneMovie
    public MovieResponse findOneMovie(MovieRequest movieRequest) throws Exception {
        return new MovieResponse(
                movieRepository
                .findById(movieRequest.getId())
                .orElseThrow(() -> new Exception("noMovie")));
    }

    //findAllReview moiveid 가져와서 찾기 querydsl  생성
    public List<ReviewResponse> findAllReview(){
        return reviewRepository.findAll()
                .stream()
                .map(ReviewResponse :: new)
                .collect(Collectors.toList());
    }

    // By Director,Name,Actor,Date & orderBy Ratings,Score,Audience
    public List<MovieResponse> findByCondition (MovieCondition condition){
        return movieRepository.findByCondition(condition)
                .stream()
                .map(MovieResponse :: new)
                .collect(Collectors.toList());
    }



}
