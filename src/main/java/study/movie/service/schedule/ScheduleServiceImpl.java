package study.movie.service.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.schedule.Schedule;
import study.movie.dto.schedule.CreateScheduleRequest;
import study.movie.dto.schedule.ScheduleResponse;
import study.movie.dto.schedule.ScheduleSearchCond;
import study.movie.repository.movie.MovieRepository;
import study.movie.repository.schedule.ScheduleRepository;
import study.movie.repository.theater.ScreenRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleServiceImpl {
    private final ScheduleRepository scheduleRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;

    @Transactional
    public ScheduleResponse save(CreateScheduleRequest request) {
        Schedule savedSchedule = Schedule.builder()
                .screen(screenRepository
                        .findById(request.getScreenId())
                        .orElseThrow(IllegalArgumentException::new))
                .movie(movieRepository
                        .findById(request.getMovieId())
                        .orElseThrow(IllegalArgumentException::new))
                .startTime(request.getStartTime())
                .build();
        return new ScheduleResponse(savedSchedule);
    }

    public List<ScheduleResponse> findAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(ScheduleResponse::new)
                .collect(Collectors.toList());
    }

    public ScheduleResponse searchSchedules(ScheduleSearchCond cond){
        scheduleRepository.searchSchedules(cond);
        return null;
    }
}
