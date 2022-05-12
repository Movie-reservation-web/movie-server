package study.movie.repository.movie;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.FilmRating;
import study.movie.domain.movie.Movie;
import study.movie.domain.movie.MovieGenre;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.Screen;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.theater.Theater;
import study.movie.repository.schedule.ScheduleRepository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static study.movie.dto.movie.condition.MovieChartSortType.AUDIENCE_DESC;
import static study.movie.dto.movie.condition.MovieChartSortType.SCORE_DESC;

@SpringBootTest
@Transactional
@Slf4j
class MovieRepositoryTest {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    InitService init;

    @Autowired
    EntityManager em;

    @Test
    void 영화_생성() {
        // given
        List<String> actors = Arrays.asList("로버트 패틴슨", "폴 다노");

        List<MovieGenre> genres = Arrays.asList(MovieGenre.ACTION, MovieGenre.ADVENTURE);
        List<FilmFormat> types = Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX);

        Movie movie = Movie.builder()
                .title("더 배트맨")
                .runningTime(176)
                .director("맷 리브스")
                .actors(actors)
                .genres(genres)
                .formats(types)
                .filmRating(FilmRating.UNDETERMINED)
                .nation("한국")
                .releaseDate(LocalDate.of(2022, 3, 1))
                .info("영웅이 될 것인가 악당이 될 것인가")
                .image("이미지")
                .build();

        Movie savedMovie = movieRepository.save(movie);

        // when
        Movie findMovie = movieRepository.findById(savedMovie.getId()).get();

        // then
        assertThat(findMovie).isEqualTo(savedMovie);
    }

    @Test
    void 영화_조회_감독이름() {
        // given
        String director = "홍길동";
        Movie movie1 = init.createMovie("영화1", director, Arrays.asList(FilmFormat.SCREEN_X, FilmFormat.TWO_D), Arrays.asList("배우"));
        Movie movie2 = init.createMovie("영화2", director, Arrays.asList(FilmFormat.SCREEN_X, FilmFormat.TWO_D), Arrays.asList("배우"));
        Movie movie3 = init.createMovie("영화3", "아무개", Arrays.asList(FilmFormat.SCREEN_X, FilmFormat.TWO_D), Arrays.asList("배우"));

        em.clear();

        // when
        List<Movie> findMovies = movieRepository.findByDirector(director);

        // then
        for (Movie findMovie : findMovies) {
            assertThat(findMovie.getDirector()).isEqualTo(director);
        }
    }

    @Test
    void 영화_조회_배우이름() {
        // given
        String actor = "배우1";
        Movie movie1 = init.createMovie("영화1", "홍길동",
                Arrays.asList(FilmFormat.SCREEN_X, FilmFormat.TWO_D),
                Arrays.asList(actor, "배우2", "배우3"));
        Movie movie2 = init.createMovie("영화2", "홍길동",
                Arrays.asList(FilmFormat.SCREEN_X, FilmFormat.TWO_D),
                Arrays.asList(actor, "배우5", "배우6"));
        Movie movie3 = init.createMovie("영화3", "아무개",
                Arrays.asList(FilmFormat.SCREEN_X, FilmFormat.TWO_D),
                Arrays.asList("배우9", "배우8", "배우7"));

        em.clear();

        // when
        List<Movie> findMovies = movieRepository.findByActor(actor);

        // then
        for (Movie findMovie : findMovies) {
            assertThat(findMovie.getActors()).contains(actor);
        }
    }

    @Test
    void 영화_조회_관객순_정렬() {
        // given
        Movie theBatman = init.createMovie("더 배트맨", "맷 리브스",
                Arrays.asList(FilmFormat.TWO_D, FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX),
                Arrays.asList("로버트 패틴슨", "폴 다노"));
        Movie theRoundup = init.createMovie("범죄 도시", "이상용",
                Arrays.asList(FilmFormat.TWO_D),
                Arrays.asList("마동석", "손석구"));
        Movie doctorStrange = init.createMovie("닥터 스트레인지2", "샘 레이미",
                Arrays.asList(FilmFormat.TWO_D, FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX),
                Arrays.asList("베네딕트 컴버배치", "레이첼 맥아담스"));
        Movie aboutTime = init.createMovie("어바웃 타임", "리차드 커티스",
                Arrays.asList(FilmFormat.TWO_D),
                Arrays.asList("도널 글리슨", "레이첼 맥아담스"));
        Movie xMan = init.createMovie("액스맨", "샘 킴",
                Arrays.asList(FilmFormat.TWO_D),
                Arrays.asList("패트릭 스튜어트", "휴 잭맨"));

        theBatman.addAudience(100);
        theRoundup.addAudience(90);
        doctorStrange.addAudience(80);
        aboutTime.addAudience(70);
        xMan.addAudience(60);

        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Screen screen1 = init.registerScreen("2관", ScreenFormat.TWO_D, theater, 3, 3);
        Screen screen2 = init.registerScreen("3관", ScreenFormat.TWO_D, theater, 3, 3);
        Screen screen3 = init.registerScreen("4관", ScreenFormat.TWO_D, theater, 3, 3);
        Screen screen4 = init.registerScreen("5관", ScreenFormat.TWO_D, theater, 3, 3);
        init.saveSchedule(theBatman, screen, LocalDateTime.now());
        init.saveSchedule(theRoundup, screen1, LocalDateTime.now());
        init.saveSchedule(doctorStrange, screen2, LocalDateTime.now());
        init.saveSchedule(aboutTime, screen3, LocalDateTime.now());
        init.saveSchedule(xMan, screen4, LocalDateTime.now());

        // when
        List<Movie> movies = movieRepository.findMovieBySort(AUDIENCE_DESC, false);
        int maxAudience = 100;

        // then
        for (Movie movie : movies) {
            assertThat(movie.getAudience()).isEqualTo(maxAudience);
            maxAudience -= 10;
        }
        assertThat(movies.indexOf(theBatman)).isEqualTo(0);
        assertThat(movies.indexOf(xMan)).isEqualTo(movies.size() - 1);
    }

    @Test
    void 영화_조회_평점_정렬() {
        // given
        Movie theBatman = init.createMovie("더 배트맨", "맷 리브스",
                Arrays.asList(FilmFormat.TWO_D, FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX),
                Arrays.asList("로버트 패틴슨", "폴 다노"));
        init.writeReview(theBatman, "작성자1", 10f);
        init.writeReview(theBatman, "작성자2", 8f);
        theBatman.calcAverageScore();

        Movie theRoundup = init.createMovie("범죄 도시", "이상용",
                Arrays.asList(FilmFormat.TWO_D),
                Arrays.asList("마동석", "손석구"));
        init.writeReview(theRoundup, "작성자1", 2f);
        init.writeReview(theRoundup, "작성자2", 6f);
        theRoundup.calcAverageScore();

        Movie doctorStrange = init.createMovie("닥터 스트레인지2", "샘 레이미",
                Arrays.asList(FilmFormat.TWO_D, FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX),
                Arrays.asList("베네딕트 컴버배치", "레이첼 맥아담스"));
        init.writeReview(doctorStrange, "작성자1", 6f);
        init.writeReview(doctorStrange, "작성자2", 4f);
        doctorStrange.calcAverageScore();

        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Screen screen1 = init.registerScreen("2관", ScreenFormat.TWO_D, theater, 3, 3);
        Screen screen2 = init.registerScreen("3관", ScreenFormat.TWO_D, theater, 3, 3);
        init.saveSchedule(theBatman, screen, LocalDateTime.now());
        init.saveSchedule(theRoundup, screen1, LocalDateTime.now());
        init.saveSchedule(doctorStrange, screen2, LocalDateTime.now());

        // when
        List<Movie> movies = movieRepository.findMovieBySort(SCORE_DESC, false);


        // then
        assertThat(movies.indexOf(theBatman)).isEqualTo(0);
        assertThat(movies.indexOf(theRoundup)).isEqualTo(movies.size() - 1);
    }

    @Test
    void 개봉한_영화_조회_관객순_정렬() {
        // given
        Movie theBatman = init.createMovie("더 배트맨", "맷 리브스",
                Arrays.asList(FilmFormat.TWO_D, FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX),
                Arrays.asList("로버트 패틴슨", "폴 다노"));
        theBatman.update(FilmRating.PG_15, LocalDate.now().minusDays(1), "intro", "image");
        Movie theRoundup = init.createMovie("범죄 도시", "이상용",
                Arrays.asList(FilmFormat.TWO_D),
                Arrays.asList("마동석", "손석구"));
        Movie doctorStrange = init.createMovie("닥터 스트레인지2", "샘 레이미",
                Arrays.asList(FilmFormat.TWO_D, FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX),
                Arrays.asList("베네딕트 컴버배치", "레이첼 맥아담스"));
        Movie aboutTime = init.createMovie("어바웃 타임", "리차드 커티스",
                Arrays.asList(FilmFormat.TWO_D),
                Arrays.asList("도널 글리슨", "레이첼 맥아담스"));
        aboutTime.update(FilmRating.PG_15, LocalDate.now().minusDays(1), "intro", "image");
        Movie xMan = init.createMovie("액스맨", "샘 킴",
                Arrays.asList(FilmFormat.TWO_D),
                Arrays.asList("패트릭 스튜어트", "휴 잭맨"));

        theBatman.addAudience(100);
        theRoundup.addAudience(90);
        doctorStrange.addAudience(80);
        aboutTime.addAudience(70);
        xMan.addAudience(60);

        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Screen screen1 = init.registerScreen("2관", ScreenFormat.TWO_D, theater, 3, 3);
        Screen screen2 = init.registerScreen("3관", ScreenFormat.TWO_D, theater, 3, 3);
        Screen screen3 = init.registerScreen("4관", ScreenFormat.TWO_D, theater, 3, 3);
        Screen screen4 = init.registerScreen("5관", ScreenFormat.TWO_D, theater, 3, 3);
        init.saveSchedule(theBatman, screen, LocalDateTime.now());
        init.saveSchedule(theRoundup, screen1, LocalDateTime.now());
        init.saveSchedule(doctorStrange, screen2, LocalDateTime.now());
        init.saveSchedule(aboutTime, screen3, LocalDateTime.now());
        init.saveSchedule(xMan, screen4, LocalDateTime.now());


        // when
        List<Movie> movies = movieRepository.findMovieBySort(AUDIENCE_DESC, true);

        // then
        assertThat(movies.size()).isEqualTo(2);
        assertThat(movies.get(0).getAudience()).isEqualTo(theBatman.getAudience());
        assertThat(movies.get(1).getAudience()).isEqualTo(aboutTime.getAudience());
        for (Movie movie : movies) {
            assertThat(movie.getReleaseDate().isBefore(LocalDate.now())).isTrue();
        }
    }

    @Test
    void 개봉한_영화_조회_평점_정렬() {
        // given
        Movie theBatman = init.createMovie("더 배트맨", "맷 리브스",
                Arrays.asList(FilmFormat.TWO_D, FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX),
                Arrays.asList("로버트 패틴슨", "폴 다노"));
        init.writeReview(theBatman, "작성자1", 10f);
        init.writeReview(theBatman, "작성자2", 8f);
        theBatman.calcAverageScore();
        theBatman.update(FilmRating.PG_15, LocalDate.now().minusDays(1), "intro", "image");

        Movie theRoundup = init.createMovie("범죄 도시", "이상용",
                Arrays.asList(FilmFormat.TWO_D),
                Arrays.asList("마동석", "손석구"));
        init.writeReview(theRoundup, "작성자1", 8f);
        init.writeReview(theRoundup, "작성자2", 6f);
        theRoundup.calcAverageScore();

        Movie doctorStrange = init.createMovie("닥터 스트레인지2", "샘 레이미",
                Arrays.asList(FilmFormat.TWO_D, FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX),
                Arrays.asList("베네딕트 컴버배치", "레이첼 맥아담스"));
        init.writeReview(doctorStrange, "작성자1", 6f);
        init.writeReview(doctorStrange, "작성자2", 4f);
        doctorStrange.calcAverageScore();
        doctorStrange.update(FilmRating.PG_15, LocalDate.now().minusDays(1), "intro", "image");

        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Screen screen1 = init.registerScreen("2관", ScreenFormat.TWO_D, theater, 3, 3);
        Screen screen2 = init.registerScreen("3관", ScreenFormat.TWO_D, theater, 3, 3);
        init.saveSchedule(theBatman, screen, LocalDateTime.now());
        init.saveSchedule(theRoundup, screen1, LocalDateTime.now());
        init.saveSchedule(doctorStrange, screen2, LocalDateTime.now());

        // when
        List<Movie> movies = movieRepository.findMovieBySort(SCORE_DESC, true);

        // then
        assertThat(movies.size()).isEqualTo(2);
        assertThat(movies.get(0).getAvgScore()).isEqualTo(theBatman.getAvgScore());
        assertThat(movies.get(1).getAvgScore()).isEqualTo(doctorStrange.getAvgScore());
        for (Movie movie : movies) {
            assertThat(movie.getReleaseDate()).isBefore(LocalDate.now());
        }
    }

    @Test
    void 미개봉_영화_조회() {
        // given
        Movie theBatman = init.createMovie("더 배트맨", "맷 리브스",
                Arrays.asList(FilmFormat.TWO_D, FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX),
                Arrays.asList("로버트 패틴슨", "폴 다노"));
        theBatman.update(FilmRating.PG_15, LocalDate.now().plusDays(1), "intro", "image");

        Movie theRoundup = init.createMovie("범죄 도시", "이상용",
                Arrays.asList(FilmFormat.TWO_D),
                Arrays.asList("마동석", "손석구"));
        theRoundup.update(FilmRating.PG_15, LocalDate.now().minusDays(1), "intro", "image");

        Movie doctorStrange = init.createMovie("닥터 스트레인지2", "샘 레이미",
                Arrays.asList(FilmFormat.TWO_D, FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX),
                Arrays.asList("베네딕트 컴버배치", "레이첼 맥아담스"));
        doctorStrange.update(FilmRating.PG_15, LocalDate.now().plusDays(2), "intro", "image");

        List<Movie> savedMovies = Arrays.asList(theBatman, theRoundup, doctorStrange);
        // when
        List<Movie> unreleasedSavedMovies = savedMovies.stream()
                .filter(movie -> movie.getReleaseDate().isAfter(LocalDate.now()))
                .collect(Collectors.toList());
        List<Movie> movies = movieRepository.findUnreleasedMovies();

        // then
        assertThat(movies.size()).isEqualTo(unreleasedSavedMovies.size());
        assertThat(movies.stream().allMatch(movie -> movie.getReleaseDate().isAfter(LocalDate.now())))
                .isTrue();
        for (int i = 0; i < movies.size() - 2; i++) {
            assertThat(movies.get(i).getReleaseDate()).isBeforeOrEqualTo(movies.get(i + 1).getReleaseDate());
        }
    }

    @Test
    void 예매_가능한_영화_조회() {
        // given
        Movie theBatman = init.createMovie("더 배트맨", "맷 리브스",
                Arrays.asList(FilmFormat.TWO_D, FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX),
                Arrays.asList("로버트 패틴슨", "폴 다노"));

        Movie theRoundup = init.createMovie("범죄 도시", "이상용",
                Arrays.asList(FilmFormat.TWO_D),
                Arrays.asList("마동석", "손석구"));

        Movie doctorStrange = init.createMovie("닥터 스트레인지2", "샘 레이미",
                Arrays.asList(FilmFormat.TWO_D, FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX),
                Arrays.asList("베네딕트 컴버배치", "레이첼 맥아담스"));

        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Screen screen1 = init.registerScreen("2관", ScreenFormat.TWO_D, theater, 3, 3);
        init.saveSchedule(theBatman, screen, LocalDateTime.now());
        init.saveSchedule(theRoundup, screen1, LocalDateTime.now());

        // when

        List<Movie> movies = movieRepository.findMovieByOpenStatus();
        List<Schedule> allSchedules = scheduleRepository.findAll();

        // then
        assertThat(movies.size()).isEqualTo(2);
        for (Movie movie : movies) {
            assertThat(
                    allSchedules.stream()
                            .anyMatch(schedule -> schedule.getMovie().equals(movie))
            ).isTrue();
        }
    }


}