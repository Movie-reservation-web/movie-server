package study.movie.service.movie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.Movie;
import study.movie.domain.ticket.Ticket;
import study.movie.dto.schedule.response.SimpleMovieResponse;
import study.movie.repository.movie.MovieRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

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
}
