package study.movie.repository.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.FilmRating;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.MovieGenre;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.Screen;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.theater.Theater;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;

@SpringBootTest
@Transactional
@Slf4j
@Commit
class SeatRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    ScheduleRepository scheduleRepository;

    private Theater createTheater(String theaterName, CityCode city, String phone) {
        Theater theater = Theater.builder()
                .name(theaterName)
                .city(city)
                .phone(phone)
                .build();
        em.persist(theater);
        return theater;
    }

    private Screen registerScreen(String screenName, ScreenFormat format, Theater theater, int maxCols, int maxRows) {
        return Screen.builder()
                .name(screenName)
                .format(format)
                .theater(theater)
                .maxCols(maxCols)
                .maxRows(maxRows)
                .build();
    }

    private Movie createMovie(String title, String director) {
        Movie movie = Movie.builder()
                .title(title)
                .director(director)
                .actors(Arrays.asList("aa", "bb"))
                .formats(Arrays.asList(FilmFormat.TWO_D, FilmFormat.IMAX))
                .filmRating(FilmRating.G_RATED)
                .genres(Arrays.asList(MovieGenre.values()[0], MovieGenre.values()[1]))
                .image("abc.jpg")
                .info("information")
                .nation("korea")
                .runningTime(160)
                .releaseDate(LocalDate.now())
                .build();
        em.persist(movie);
        return movie;
    }

//    @Test
//    public void 전체_좌석_조회() throws Exception {
//        // given
//        Theater theater = createTheater("용산 CGV", CityCode.SEL, "000-000");
//        Screen screen = registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
//        Movie movie = createMovie("영화1", "홍길동");
//
//        ScreenTime screenTime = new ScreenTime(LocalDateTime.of(2022, 3, 10, 3, 2, 21), movie.getRunningTime());
//        Schedule savedSchedule = Schedule.builder()
//                .screenTime(screenTime)
//                .screen(screen)
//                .movie(movie)
//                .build();
//
//        em.flush();
//        Long savedId = savedSchedule.getId();
//
//        // when
//        int size = savedSchedule.getSeats().size();
//        List<SeatEntity> seatByQuery = scheduleRepository.findScheduleWithSeat(savedId);
//
//        // then
//        assertEquals(size, seatByQuery.size());
//
//    }


}