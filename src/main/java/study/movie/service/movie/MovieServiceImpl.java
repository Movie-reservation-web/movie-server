package study.movie.service.movie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.Movie;
import study.movie.domain.ticket.Ticket;
import study.movie.dto.movie.*;
import study.movie.dto.schedule.response.MovieChartResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.utils.BasicServiceUtils;
import study.movie.repository.movie.MovieRepository;
import study.movie.repository.movie.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

import static study.movie.global.exception.ErrorCode.MOVIE_NOT_FOUND;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MovieServiceImpl extends BasicServiceUtils implements MovieService {
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public PostIdResponse saveMovie(CreateMovieRequest request) {
        Movie savedMovie = movieRepository.save(request.toEntity());
        return PostIdResponse.of(savedMovie.getId());
    }

    @Transactional
    public void updateMovie(UpdateMovieRequest request) {
        //findMovie 생성 후
        Movie findMovie = movieRepository
                .findById(request.getId())
                .orElseThrow(getExceptionSupplier(MOVIE_NOT_FOUND));

        //findMovie.update(변경될 피드들만 파라미터)
        findMovie.update(request.getFilmRating(), request.getReleaseDate(),
                request.getInfo(), request.getImage());

    }

    @Transactional
    public void deleteMovie(Long movieId) {
        movieRepository.deleteById(movieId);
    }


    //findOneMovie
    public BasicMovieResponse findOneMovie(Long movieId) {
        return BasicMovieResponse.of(
                movieRepository
                        .findById(movieId)
                        .orElseThrow(getExceptionSupplier(MOVIE_NOT_FOUND)));
    }

    // By Director,Name,Actor & orderBy Date Default
    public List<FindMovieResponse> findMovieByActor(String actor) {
//        return movieRepository.findByActorsContains(actor)
//                .stream()
//                .map(FindMovieResponse::new)
//                .collect(Collectors.toList());
        return null;
    }

    public List<FindMovieResponse> findMovieByDirector(String director) {
        return movieRepository.findByDirector(director)
                .stream()
                .map(FindMovieResponse::new)
                .collect(Collectors.toList());
    }

    //상영예정작보기
    public List<MovieChartResponse> findUnreleasedMovies() {
        List<Movie> movies = movieRepository.findUnreleasedMovies();
        double totalCount = getTotalAudienceCount(movies);
        return movies
                .stream()
                .map(movie -> MovieChartResponse.of(movie, totalCount))
                .collect(Collectors.toList());
    }


    //영화 차트 보기_orderBy Ratings,Score,Audience
    public List<MovieChartResponse> findMovieBySort(MovieSortType sortType, boolean isReleased) {
        List<Movie> movies = movieRepository.findMovieBySort(sortType, isReleased);
        double totalCount = getTotalAudienceCount(movies);
        return movies.stream()
                .map(movie -> MovieChartResponse.of(movie, totalCount))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void updateMovieAudience() {
        List<Movie> movies = movieRepository.findMovieByOpenStatus();
        for (Movie movie : movies) {
            movie.addAudience(
                    movie.getTickets().stream().mapToInt(Ticket::getReservedMemberCount).sum());
            movie.calcAverageScore();
        }
    }

    private double getTotalAudienceCount(List<Movie> movies) {
        return movies.stream().mapToDouble(Movie::getAudience).sum();
    }
}
