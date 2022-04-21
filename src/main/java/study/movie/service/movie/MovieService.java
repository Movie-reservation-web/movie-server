package study.movie.service.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.Review;
import study.movie.dto.movie.MovieRequest;
import study.movie.dto.movie.MovieResponse;
import study.movie.dto.movie.ReviewRequest;
import study.movie.dto.movie.ReviewResponse;
import study.movie.repository.movie.MovieRepository;
import study.movie.repository.movie.MovieRepositoryImpl;
import study.movie.repository.movie.ReviewRepository;
import study.movie.repository.movie.ReviewRepositoryImpl;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    //saveMovie
    @Transactional
    public MovieResponse saveMovie(MovieRequest movieRequest){
        //영화 생성 시  reivews, schedules 값은 제외
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
    @Transactional
    public ReviewResponse saveReview(ReviewRequest reviewRequest) throws Exception {
        Movie findMovie = movieRepository
                .findById(reviewRequest.getMovieId())
                .orElseThrow(() -> new Exception("noMovie"));
                //.orElseThrow(getExceptionSupplier());

        Review createReview = Review.builder()
                .movie(findMovie)
                .writer(reviewRequest.getWriter())
                .score(reviewRequest.getScore())
                .comment(reviewRequest.getComment())
                .build();

        return new ReviewResponse(createReview);
    }



    //updateMovie

    //updateReview

    //deleteMovie

    //deleteReview

    //byDirector,Name,Actor,date
    public List<MovieResponse> findByValue (String value){
        movieRepository.findByDirector();
    }

    //findOneMovie

    //findAllMovieOrderBy 관람객, 예매율, 평점


}
