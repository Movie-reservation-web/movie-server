package study.movie.domain.movie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.dto.request.CreateMovieRequest;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.movie.repository.MovieRepository;
import study.movie.domain.ticket.entity.Ticket;
import study.movie.domain.movie.dto.condition.MovieChartSortType;
import study.movie.domain.movie.dto.condition.MovieSearchCond;
import study.movie.domain.movie.dto.condition.MovieSearchType;
import study.movie.domain.movie.dto.condition.MovieSortType;
import study.movie.domain.movie.dto.request.UpdateMovieRequest;
import study.movie.domain.movie.dto.response.BasicMovieResponse;
import study.movie.domain.movie.dto.response.FindMovieResponse;
import study.movie.domain.movie.dto.response.MovieSearchResponse;
import study.movie.domain.schedule.dto.response.MovieChartResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.DomainSpec;
import study.movie.global.paging.PageableDTO;
import study.movie.global.utils.BasicServiceUtil;

import java.util.List;
import java.util.stream.Collectors;

import static study.movie.exception.ErrorCode.MOVIE_NOT_FOUND;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieServiceImpl extends BasicServiceUtil implements MovieService {
    private final MovieRepository movieRepository;
    private final DomainSpec<MovieSortType> spec = new DomainSpec<>(MovieSortType.class);

    @Override
    public List<MovieChartResponse> findMovieBySort(MovieChartSortType sortType, boolean isReleased) {
        List<Movie> movies = movieRepository.findMovieBySort(sortType, isReleased);
        double totalCount = getTotalAudienceCount(movies);
        return movies.stream()
                .map(movie -> MovieChartResponse.of(movie, totalCount))
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieChartResponse> findUnreleasedMovies() {
        List<Movie> movies = movieRepository.findUnreleasedMovies();
        double totalCount = getTotalAudienceCount(movies);
        return movies
                .stream()
                .map(movie -> MovieChartResponse.of(movie, totalCount))
                .collect(Collectors.toList());
    }

    @Override
    public BasicMovieResponse findOneMovie(Long movieId) {
        return BasicMovieResponse.of(
                movieRepository
                        .findById(movieId)
                        .orElseThrow(getExceptionSupplier(MOVIE_NOT_FOUND)));
    }

    @Override
    public List<FindMovieResponse> findMovieByActor(String actor) {
        return movieRepository.findByActor(actor)
                .stream()
                .map(FindMovieResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    public List<FindMovieResponse> findMovieByDirector(String director) {
        return movieRepository.findByDirector(director)
                .stream()
                .map(FindMovieResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PostIdResponse save(CreateMovieRequest request) {
        Movie savedMovie = movieRepository.save(request.toEntity());
        return PostIdResponse.of(savedMovie.getId());
    }

    @Override
    @Transactional
    public void update(Long id, UpdateMovieRequest request) {
        Movie findMovie = movieRepository
                .findById(id)
                .orElseThrow(getExceptionSupplier(MOVIE_NOT_FOUND));

        findMovie.update(request.getFilmRating(), request.getReleaseDate(),
                request.getInfo(), request.getImage());

    }

    @Override
    @Transactional
    public void delete(Long movieId) {
        movieRepository.deleteById(movieId);
    }

    @Override
    public Page<MovieSearchResponse> search(MovieSearchType type, MovieSearchCond cond, PageableDTO pageableDTO) {
        Page<Movie> result = getSchedulesByCondType(type, cond, spec.getPageable(pageableDTO));
        return result.map(MovieSearchResponse::of);
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

    private Page<Movie> getSchedulesByCondType(MovieSearchType type, MovieSearchCond cond, Pageable pageable) {
        switch (type) {
            case ACTOR:
                return movieRepository.findByActorPaging(cond.getActor(), pageable);
            case FILM_FORMAT:
                return movieRepository.findByFormatPaging(cond.getFilmFormat(), pageable);
            default:
                return movieRepository.search(cond, pageable);
        }
    }

    private double getTotalAudienceCount(List<Movie> movies) {
        return movies.stream().mapToDouble(Movie::getAudience).sum();
    }

}
