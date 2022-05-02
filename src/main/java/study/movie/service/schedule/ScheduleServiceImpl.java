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
import study.movie.domain.theater.Screen;
import study.movie.dto.schedule.condition.ScheduleBasicSearchCond;
import study.movie.dto.schedule.condition.ScheduleSearchCond;
import study.movie.dto.schedule.request.CreateScheduleRequest;
import study.movie.dto.schedule.request.ScheduleScreenRequest;
import study.movie.dto.schedule.response.*;
import study.movie.global.dto.IdListRequest;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.page.DomainSpec;
import study.movie.global.page.PageableDTO;
import study.movie.global.utils.BasicServiceUtils;
import study.movie.repository.movie.MovieRepository;
import study.movie.repository.schedule.ScheduleRepository;
import study.movie.repository.schedule.SeatRepository;
import study.movie.repository.theater.ScreenRepository;
import study.movie.entitySearchStrategy.schedule.ScheduleMetaType;
import study.movie.entitySearchStrategy.schedule.ScheduleSortStrategy;

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
    public PostIdResponse save(CreateScheduleRequest request) {
        // 영화 조회
        Movie findMovie = movieRepository
                .findById(request.getMovieId())
                .orElseThrow(getExceptionSupplier(MOVIE_NOT_FOUND));
        Screen screen = screenRepository
                .findById(request.getScreenId())
                .orElseThrow(getExceptionSupplier(SCHEDULE_NOT_FOUND));
        Schedule schedule = Schedule.builder()
                .movie(findMovie)
                .screen(screen)
                .screenTime(new ScreenTime(request.getStartTime(), findMovie.getRunningTime()))
                .build();
        Schedule savedSchedule = scheduleRepository.save(schedule);

        return PostIdResponse.of(savedSchedule.getId());
    }

    @Override
    public Page<ScheduleResponse> search(ScheduleSearchCond cond, PageableDTO pageableDTO) {
        Pageable pageable = spec.getPageable(pageableDTO);
        return scheduleRepository.search(cond, pageable)
                .map(ScheduleResponse::of);
    }
    @Override
    public Page<ScheduleResponse> search2(ScheduleSearchCond cond, PageableDTO pageableDTO) {
        Pageable pageable = spec.getPageable(pageableDTO);
        return scheduleRepository.search2(cond, pageable);

    }

    @Override
    @Transactional
    public void closeSchedule(LocalDateTime dateTime) {
        scheduleRepository.updateStatusByPastDaysStatus(dateTime);
    }

    @Override
    @Transactional
    public void removeSchedule(IdListRequest request) {
        seatRepository.deleteAllByScheduleIdInQuery(request.getIds());
        scheduleRepository.deleteAllByIdInQuery(request.getIds());
    }
}
