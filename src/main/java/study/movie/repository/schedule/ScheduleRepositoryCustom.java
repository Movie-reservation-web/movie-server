package study.movie.repository.schedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.SeatEntity;
import study.movie.domain.theater.ScreenFormat;
import study.movie.dto.schedule.condition.ScheduleBasicSearchCond;
import study.movie.dto.schedule.condition.ScheduleSearchCond;
import study.movie.dto.schedule.condition.UpdateSeatRequest;
import study.movie.dto.schedule.request.ScheduleScreenRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepositoryCustom {

    // API
    /**
     * 모든 상영일정 조회
     *
     * @return
     */
    List<Schedule> findAllSchedules();

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
    List<SeatEntity> findSeatByScheduleId(Long scheduleId);

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
     * @param totalElements
     * @return List
     */
    Page<Schedule> search(ScheduleSearchCond cond, Pageable pageable, Integer totalElements);

    /**
     * 지난 시각의 상영일정 상태 변경
     *
     * @param dateTime
     */
    void updateStatusByPastDateTime(LocalDateTime dateTime);

}
