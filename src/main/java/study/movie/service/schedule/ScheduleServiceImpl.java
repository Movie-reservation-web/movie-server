package study.movie.service.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.Movie;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.ScreenTime;
import study.movie.dto.schedule.*;
import study.movie.repository.movie.MovieRepository;
import study.movie.repository.schedule.ScheduleRepository;
import study.movie.repository.theater.ScreenRepository;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleServiceImpl {
    private final ScheduleRepository scheduleRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;

    /**
     * 상영일정 저장
     */
    @Transactional
    public CreateScheduleResponse save(CreateScheduleRequest request) {
        // 영화 조회
        Movie findMovie = movieRepository
                .findById(request.getMovieId())
                .orElseThrow(IllegalArgumentException::new);

        Schedule savedSchedule = Schedule.builder()
                .movie(findMovie)
                .screen(screenRepository
                        .findById(request.getScreenId())
                        .orElseThrow(IllegalArgumentException::new))
                .screenTime(new ScreenTime(request.getStartTime(), findMovie.getRunningTime()))
                .build();
        return new CreateScheduleResponse(savedSchedule);
    }

    /**
     * 모든 상영 일정 조회
     */
    public List<ScheduleSearchResponse> findAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(ScheduleSearchResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 상영 일정 조회
     */
    public List<? extends BaseScheduleResponse> searchSchedules(ScheduleSearchCond cond) {
        return scheduleRepository.searchSchedules(cond).stream()
                .map(checkResponse(cond.isFinalSearch()))
                .distinct()
                .collect(Collectors.toList());
    }
    
    /**
     * 상영 일정 조회 - 영화 선택
     */
    public MovieFormatResponse searchScheduleByMovie(String movieTitle){
        return new MovieFormatResponse(movieTitle, scheduleRepository.findFormatByMovie(movieTitle));
    }


    /**
     * search condition 에 맞는 영화 포멧이 존재하는지 필터링
     */
    private boolean filterFormats(Set<FilmFormat> filmFormat, FilmFormat format) {
        return format == null || filmFormat.contains(format);
    }

    /**
     * search condition 에 맞춰 return response 판별
     */
    private Function<Schedule, ? extends BaseScheduleResponse> checkResponse(Boolean finalSearchCond) {
        return finalSearchCond ? ScheduleScreenResponse::new : ScheduleSearchResponse::new;
    }

    /**
     * 상영 일정 삭제
     */
    @Transactional
    public void removeSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }


}
