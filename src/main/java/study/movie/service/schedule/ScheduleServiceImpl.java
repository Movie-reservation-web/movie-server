package study.movie.service.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.Movie;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.ScreenTime;
import study.movie.dto.schedule.condition.ScheduleBasicSearchCond;
import study.movie.dto.schedule.condition.ScheduleSearchCond;
import study.movie.dto.schedule.request.CreateScheduleRequest;
import study.movie.dto.schedule.request.ScheduleScreenRequest;
import study.movie.dto.schedule.response.*;
import study.movie.global.dto.IdListRequest;
import study.movie.global.page.DomainSpec;
import study.movie.global.page.PageableDTO;
import study.movie.global.utils.BasicServiceUtils;
import study.movie.repository.movie.MovieRepository;
import study.movie.repository.schedule.ScheduleRepository;
import study.movie.repository.schedule.SeatRepository;
import study.movie.repository.theater.ScreenRepository;
import study.movie.sortStrategy.schedule.ScheduleMetaType;
import study.movie.sortStrategy.schedule.ScheduleSortStrategy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static study.movie.global.exception.ErrorCode.MOVIE_NOT_FOUND;
import static study.movie.global.exception.ErrorCode.SCHEDULE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ScheduleServiceImpl extends BasicServiceUtils implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;
    private final DomainSpec<ScheduleMetaType> spec = new DomainSpec<>(ScheduleMetaType.class, new ScheduleSortStrategy());

    @Override
    public List<ScheduleSearchResponse> findAllSchedules() {
        return scheduleRepository.findAllSchedules().stream()
                .map(ScheduleSearchResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleSearchResponse> searchBasicSchedules(ScheduleBasicSearchCond cond) {
        return scheduleRepository.searchBasicSchedules(cond).stream()
                .map(ScheduleSearchResponse::of)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleScreenResponse> searchScheduleScreens(ScheduleScreenRequest request) {
        return scheduleRepository.searchScheduleScreens(request).stream()
                .map(ScheduleScreenResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    public MovieFormatResponse searchScheduleByMovie(String movieTitle) {
        return MovieFormatResponse.of(movieTitle, scheduleRepository.findFormatByMovie(movieTitle));
    }

    @Override
    public List<SeatResponse> getScheduleSeatEntity(Long scheduleId) {
        return scheduleRepository.findSeatByScheduleId(scheduleId).stream()
                .map(SeatResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CreateScheduleResponse save(CreateScheduleRequest request) {
        // 영화 조회
        Movie findMovie = movieRepository
                .findById(request.getMovieId())
                .orElseThrow(getExceptionSupplier(MOVIE_NOT_FOUND));
        Schedule savedSchedule = Schedule.builder()
                .movie(findMovie)
                .screen(screenRepository
                        .findById(request.getScreenId())
                        .orElseThrow(getExceptionSupplier(SCHEDULE_NOT_FOUND)))
                .screenTime(new ScreenTime(request.getStartTime(), findMovie.getRunningTime()))
                .build();

        return CreateScheduleResponse.of(savedSchedule);
    }

    @Override
    public Page<ScheduleResponse> search(ScheduleSearchCond cond, PageableDTO pageableDTO) {
        Pageable pageable = spec.getPageable(pageableDTO);
        return scheduleRepository.search(cond, pageable, pageableDTO.getTotalElements()).map(ScheduleResponse::of);
    }

    @Override
    @Transactional
    public void closeSchedule(LocalDateTime dateTime) {
        scheduleRepository.updateStatusByPastDateTime(dateTime);
    }

    @Override
    @Transactional
    public void removeSchedule(IdListRequest request) {
        seatRepository.deleteAllByScheduleIdInQuery(request.getIds());
        scheduleRepository.deleteAllByIdInQuery(request.getIds());
    }

    // MovieService로 이동해야함.

    /**
     * 상영중인 영화 차트
     */
    public List<SimpleMovieResponse> findAllOpenMovies() {
        List<Movie> movies = scheduleRepository.findMovieByOpenStatus();
        double totalCount = movies.stream().mapToDouble(Movie::getAudience).sum();
        return movies.stream()
                .map(movie -> SimpleMovieResponse.of(movie, totalCount))
                .collect(Collectors.toList());
    }

}
