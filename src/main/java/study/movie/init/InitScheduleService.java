package study.movie.init;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.movie.repository.MovieRepository;
import study.movie.domain.schedule.dto.request.CreateScheduleRequest;
import study.movie.domain.schedule.entity.Schedule;
import study.movie.domain.schedule.entity.ScreenTime;
import study.movie.domain.schedule.repository.ScheduleRepository;
import study.movie.domain.theater.entity.Screen;
import study.movie.domain.theater.repository.ScreenRepository;
import study.movie.global.utils.JsonUtil;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InitScheduleService {
    private static final String SCHEDULE = "schedule";
    private final ScheduleRepository scheduleRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;

    @Transactional
    public void initScheduleData() {
        scheduleRepository.saveAll(
                JsonUtil.jsonArrayToList(SCHEDULE, CreateScheduleRequest.class).stream()
                        .map(this::mapToSchedule)
                        .collect(Collectors.toList()
                        )
        );
    }

    private Schedule mapToSchedule(CreateScheduleRequest request) {
        Movie findMovie = movieRepository
                .findAll().get((int) (request.getMovieId() - 1));
        Screen screen = screenRepository
                .findAll().get((int) (request.getScreenId() - 1));

        Schedule schedule = Schedule.builder()
                .movie(findMovie)
                .screen(screen)
                .screenTime(new ScreenTime(request.getStartTime(), findMovie.getRunningTime()))
                .build();
        return schedule;
    }
}
