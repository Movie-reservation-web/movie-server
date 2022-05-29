package study.movie.domain.schedule.service;

import org.springframework.data.domain.Page;
import study.movie.domain.schedule.dto.condition.ScheduleBasicSearchCond;
import study.movie.domain.schedule.dto.condition.ScheduleSearchCond;
import study.movie.domain.schedule.dto.request.CreateScheduleRequest;
import study.movie.domain.schedule.dto.request.ReservationScreenRequest;
import study.movie.domain.schedule.dto.request.ScheduleScreenRequest;
import study.movie.domain.schedule.dto.response.*;
import study.movie.global.dto.IdListRequest;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.PageableDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleService {
    /**
     * Api Server
     * <p>
     * 상영중인 모든 상영 일정 조회
     */
    List<ScheduleSearchResponse> findAllSchedules();

    /**
     * Api Server
     * <p>
     * 상영 일정 조회 - 영화, 극장, 날짜
     */
    List<ScheduleSearchResponse> searchBasicSchedules(ScheduleBasicSearchCond cond);

    /**
     * Api Server
     * <p>
     * 상영 일정 조회 - 상영관
     */
    List<ScheduleScreenResponse> searchScheduleScreens(ScheduleScreenRequest request);

    /**
     * Api Server
     * <p>
     * 상영 일정 조회 - 영화 선택 시 호출
     */
    MovieFormatResponse searchScheduleByMovie(String movieTitle);

    /**
     * Api Sever
     * <p>
     * 상영일정에 대한 좌석 정보 및 AgeType에 따른 가격 정보
     */
    ReservationScreenResponse getSelectedScreenInfo(ReservationScreenRequest request);

    /**
     * Admin Server
     * <p>
     * 상영일정 저장
     */
    PostIdResponse save(CreateScheduleRequest request);

    /**
     * Admin Server
     * <p>
     * 상영 일정 조회 - 영화, 상영관 포멧, 극장, 날짜, 스케줄 상태
     */
    Page<ScheduleResponse> search(ScheduleSearchCond cond, PageableDTO pageableDTO);

    /**
     * Admin Server <p>
     * 스케줄 단건 조회
     */
    ScheduleResponse findById(Long id);

    /**
     * Batch Server
     * <p>
     * 상영 일정 상태 변경
     * 정해진 시간을 기준으로 이전 시간에 있는 상영일정의 상태를 변경
     * OPEN->CLOSED 로만 동작
     */
    void closeSchedule(LocalDateTime dateTime);

    /**
     * Batch Server
     * <p>
     * 상영 일정 삭제
     */
    void removeSchedule(IdListRequest request);
}
