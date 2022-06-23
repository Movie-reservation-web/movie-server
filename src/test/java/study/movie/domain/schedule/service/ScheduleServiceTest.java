package study.movie.domain.schedule.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.schedule.dto.condition.ScheduleBasicSearchCond;
import study.movie.domain.schedule.dto.request.CreateScheduleRequest;
import study.movie.domain.schedule.dto.request.ScheduleScreenRequest;
import study.movie.domain.schedule.dto.response.MovieFormatResponse;
import study.movie.domain.schedule.dto.response.ScheduleScreenResponse;
import study.movie.domain.schedule.dto.response.ScheduleSearchResponse;
import study.movie.domain.schedule.entity.Schedule;
import study.movie.domain.schedule.entity.ScreenTime;
import study.movie.domain.schedule.entity.SeatEntity;
import study.movie.domain.schedule.repository.ScheduleRepository;
import study.movie.domain.schedule.repository.SeatRepository;
import study.movie.domain.theater.entity.Screen;
import study.movie.domain.theater.entity.ScreenFormat;
import study.movie.domain.theater.entity.Theater;
import study.movie.global.dto.IdListRequest;
import study.movie.global.dto.PostIdResponse;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static study.movie.global.utils.NumberUtil.getRandomIndex;

@SpringBootTest
@Transactional
@Slf4j
@Rollback
class ScheduleServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    ScheduleServiceImpl scheduleService;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    SeatRepository seatRepository;

    private Schedule initialRandomSchedule;
    @BeforeEach
    void setUp() {
        initialRandomSchedule = scheduleRepository.findAll()
                .get((int) getRandomIndex(scheduleRepository.count()));
    }

    @Test
    @DisplayName("상영일정을 저장한다.")
    void 상영일정_저장(){
        // given
        Screen screenForSave = initialRandomSchedule.getScreen();
        Theater theaterForSave = screenForSave.getTheater();
        Movie movieForSave = initialRandomSchedule.getMovie();
        LocalDateTime startTime = LocalDateTime.now().plusDays(10);

        // when
        CreateScheduleRequest request = new CreateScheduleRequest();
        request.setMovieId(movieForSave.getId());
        request.setScreenId(screenForSave.getId());
        request.setStartTime(startTime);

        PostIdResponse response = scheduleService.save(request);

        Schedule savedSchedule = scheduleRepository.findById(response.getId()).get();

        // then
        assertThat(theaterForSave.getName()).isEqualTo(savedSchedule.getScreen().getTheater().getName());
        assertThat(movieForSave.getTitle()).isEqualTo(savedSchedule.getMovie().getTitle());
        assertThat(startTime).isEqualTo(savedSchedule.getScreenTime().getStartDateTime());
    }

    @Test
    @DisplayName("시간은 다르지만 날짜가 같은 객체는 중복 제거되어야 한다.")
    void 상영일정_조건_검색_중복된_값_제거(){
        // given
        Screen screenForSearch = initialRandomSchedule.getScreen();
        Theater theaterForSearch = screenForSearch.getTheater();
        Movie movieForSearch = initialRandomSchedule.getMovie();
        LocalDate screenDate = LocalDate.now();
        ScreenTime screenTime1 = new ScreenTime(screenDate.atTime(3, 2, 21),
                movieForSearch.getRunningTime());
        ScreenTime screenTime2 = new ScreenTime(screenDate.atTime(6, 2, 21),
                movieForSearch.getRunningTime());

        Schedule saveSchedule1 = Schedule.builder()
                .screenTime(screenTime1)
                .screen(screenForSearch)
                .movie(movieForSearch)
                .build();

        Schedule saveSchedule2 = Schedule.builder()
                .screenTime(screenTime2)
                .screen(screenForSearch)
                .movie(movieForSearch)
                .build();
        scheduleRepository.save(saveSchedule1);
        scheduleRepository.save(saveSchedule2);
        // when
        ScheduleBasicSearchCond cond = new ScheduleBasicSearchCond();
        cond.setTheaterId(theaterForSearch.getId());
        cond.setMovieId(movieForSearch.getId());
        cond.setScreenDate(screenDate);

        List<ScheduleSearchResponse> scheduleSearchResponse = scheduleService.searchBasicSchedules(cond);
        List<Schedule> schedules = scheduleRepository.searchBasicSchedules(cond);

        // then
        assertThat(scheduleSearchResponse.size()).isEqualTo(schedules.size() - 1);
        assertThat(scheduleSearchResponse.get(0).getScreenDate()).isEqualTo(screenDate);
    }

    @Test
    @DisplayName("영화,포멧으로 상영일정을 검색한다.")
    void 상영일정_조건_검색_기본_정보(){
        // given
        Screen screenForSearch = initialRandomSchedule.getScreen();
        Movie movieForSearch = initialRandomSchedule.getMovie();

        // 검색 조건
        ScheduleBasicSearchCond cond = new ScheduleBasicSearchCond();
        cond.setScreenFormat(screenForSearch.getFormat());
        cond.setMovieId(movieForSearch.getId());

        // when
        List<ScheduleSearchResponse> searchResponses = scheduleService.searchBasicSchedules(cond);

        // then
        for (ScheduleSearchResponse searchResponse : searchResponses) {
            assertThat(movieForSearch.getTitle()).isEqualTo(searchResponse.getMovie().getMovieTitle());
            assertThat(movieForSearch.getImage()).isEqualTo(searchResponse.getMovie().getImage());
            assertThat(movieForSearch.getFilmRating().getValue()).isEqualTo(searchResponse.getMovie().getFilmRating());
        }
    }

    @Test
    @DisplayName("극장, 영화, 상영날짜, 포멧으로 해당 상영관의 정보를 조회한다.")
    void 상영일정_조건_검색_상영관_정보(){
        // given
        Screen screenForSearch = initialRandomSchedule.getScreen();
        Theater theaterForSearch = screenForSearch.getTheater();
        Movie movieForSearch = initialRandomSchedule.getMovie();
        ScreenTime screenTime = initialRandomSchedule.getScreenTime();

        // 검색 조건
        ScheduleScreenRequest request = new ScheduleScreenRequest();
        request.setTheaterId(theaterForSearch.getId());
        request.setMovieId(movieForSearch.getId());
        request.setScreenDate(screenTime.getStartDateTime().toLocalDate());
        request.setScreenFormat(screenForSearch.getFormat());

        // when
        List<ScheduleScreenResponse> screenResponses = scheduleService.searchScheduleScreens(request);

        // then
        for (ScheduleScreenResponse screenResponse : screenResponses) {
            assertThat(screenResponse.getScreenTime().getStartDateTime().toLocalDate())
                    .isEqualTo(screenTime.getStartDateTime().toLocalDate());
        }
    }

    @Test
    void 상영일정_조회_영화_포멧(){
        // given
        Movie movieForSearch = initialRandomSchedule.getMovie();

        // when
        MovieFormatResponse response = scheduleService.searchScheduleByMovie(movieForSearch.getTitle());

        // then
        assertEquals(movieForSearch.getTitle(), response.getMovieTitle());
        for (ScreenFormat format : response.getFormats()) {
            assertThat(movieForSearch.getFormats().stream().anyMatch(format::isAllowedFilmFormat)).isTrue();
        }
    }

    @Test
    void 상영일정_삭제(){
        // given
        List<SeatEntity> seatsForDelete = initialRandomSchedule.getSeats();
        // when
        IdListRequest request = new IdListRequest();
        request.setIds(Collections.singletonList(initialRandomSchedule.getId()));

        scheduleService.removeSchedule(request);

        // then
        assertThat(scheduleRepository.existsById(initialRandomSchedule.getId())).isFalse();
        for (SeatEntity seat : seatsForDelete) {
            assertThat(seatRepository.existsById(seat.getId())).isFalse();
        }

    }
}
