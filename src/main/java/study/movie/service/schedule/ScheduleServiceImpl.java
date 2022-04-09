package study.movie.service.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.schedule.ReservationStatus;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.Seat;
import study.movie.dto.schedule.*;
import study.movie.repository.movie.MovieRepository;
import study.movie.repository.schedule.ScheduleRepository;
import study.movie.repository.theater.ScreenRepository;

import java.util.List;
import java.util.Map;
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
        Schedule savedSchedule = Schedule.builder()
                .screen(screenRepository
                        .findById(request.getScreenId())
                        .orElseThrow(IllegalArgumentException::new))
                .movie(movieRepository
                        .findById(request.getMovieId())
                        .orElseThrow(IllegalArgumentException::new))
                .startTime(request.getStartTime())
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
    public List<? extends BaseScheduleResponse> searchSchedules(ScheduleSearchCond cond){
        return scheduleRepository.searchSchedules(cond).stream()
                .filter(schedule -> cond.getFormats().contains(schedule.getScreen().getFormat().getFilmFormat().name()))
                .map(checkResponse(cond.isFinalSearch()))
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * search condition 에 맞춰 return response 판별
     */
    private Function<Schedule, ? extends BaseScheduleResponse> checkResponse(Boolean finalSearchCond){
        return finalSearchCond ? ScheduleScreenResponse::new : ScheduleSearchResponse::new;
    }

    /**
     * 상영 일정 삭제
     */
    @Transactional
    public void removeSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    public List<ScheduleSeatResponse> getAllScheduleSeat(Long id){
        Map<Seat, ReservationStatus> seats = scheduleRepository
                .findById(id)
                .orElseThrow(IllegalArgumentException::new)
                .getSeats();

        return seats.keySet().stream()
                .map(seat -> new ScheduleSeatResponse(seat, seats.get(seat)))
                .collect(Collectors.toList());
    }

}
