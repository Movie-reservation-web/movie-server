package study.movie.domain.schedule.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.movie.domain.schedule.dto.condition.ScheduleBasicSearchCond;
import study.movie.domain.schedule.entity.Schedule;
import study.movie.domain.schedule.entity.SeatEntity;
import study.movie.domain.theater.entity.ScreenFormat;
import study.movie.domain.schedule.dto.condition.ScheduleSearchCond;
import study.movie.domain.schedule.dto.request.ScheduleScreenRequest;
import study.movie.domain.schedule.dto.request.UpdateSeatRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepositoryCustom {

    // API
    /**
     * 모든 상영일정 조회
     *
     * @return
     */
    List<Schedule> findAllSchedules();

    /**
     * 해당 시간대에 상영관에 존재하는 스케줄 조회
     * @return
     */
    Optional<Schedule> findDuplicatedSchedule(Long screenId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 상영일정 검색
     * <p>
     * Fixed Condition: 현재 시각 이후의 상영일정
     *
     * @param cond movieTitle, screenFormat, theaterName, screenDate
     * @return List
     */
    List<Schedule> searchBasicSchedules(ScheduleBasicSearchCond cond);

    /**
     * 상영일정 검색 - 모든 컨디션 있어야함.
     * <p>
     * Fixed Condition: 현재 시각 이후의 상영일정
     *
     * @param cond movieTitle, screenFormat, theaterName, screenDate
     * @return List
     */
    List<Schedule> searchScheduleScreens(ScheduleScreenRequest request);

    /**
     * 좌석 상태 변경
     *
     * @param cond scheduleId, seats, status
     */
    void updateSeatStatus(UpdateSeatRequest cond);

    /**
     * 상영일정의 좌석엔티티 조회
     *
     * @param scheduleId
     * @return List
     */
    List<SeatEntity> findSeatsByScheduleId(Long scheduleId);

    /**
     * 상영중인 영화의 포멧 조회
     *
     * @param movieTitle
     * @return List
     */
    List<ScreenFormat> findFormatByMovie(String movieTitle);

    // Admin
    /**
     * 상영일정 조회
     *
     * @param cond          movieTitle, movieNation, theaterName, screenDate, screenFormat, scheduleStatus, scheduleNumber, orderBy
     * @param pageable
     * @return Page
     */
    Page<Schedule> search(ScheduleSearchCond cond, Pageable pageable);

    /**
     * 지난 시각의 상영일정 상태 변경
     *
     * @param dateTime
     */
    void updateStatusByPastDaysStatus(LocalDateTime dateTime);

}
