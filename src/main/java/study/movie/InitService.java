package study.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.member.entity.GenderType;
import study.movie.domain.member.entity.Member;
import study.movie.domain.movie.entity.*;
import study.movie.domain.schedule.entity.Schedule;
import study.movie.domain.schedule.entity.ScreenTime;
import study.movie.domain.theater.entity.CityCode;
import study.movie.domain.theater.entity.Screen;
import study.movie.domain.theater.entity.ScreenFormat;
import study.movie.domain.theater.entity.Theater;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class InitService {
    private final EntityManager em;

    public List<Screen> addTheaterScreen(String theaterName, CityCode city, List<String> screenNames, List<ScreenFormat> screenFormats) {
        List<Screen> savedScreens = new ArrayList<>();
        Theater theater = createTheater(theaterName, city);
        for (int i = 0; i < screenFormats.size(); i++) {
            savedScreens.add(registerScreen(screenNames.get(i), screenFormats.get(i), theater, 5, 5));
        }
        return savedScreens;
    }

    public List<Movie> addMovies(List<String> titles, List<String> directors, List<List<String>> actors, List<List<FilmFormat>> filmFormats) {
        List<Movie> savedMovies = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            savedMovies.add(
                    createMovie(titles.get(i), directors.get(i), filmFormats.get(i), actors.get(i))
            );
        }
        return savedMovies;
    }

    public Theater createTheater(String theaterName, CityCode city) {
        Theater theater = Theater.builder()
                .name(theaterName)
                .city(city)
                .phone("0000-0000")
                .build();
        em.persist(theater);
        return theater;
    }

    public Screen registerScreen(String screenName, ScreenFormat format, Theater theater, int maxCols, int maxRows) {
        return Screen.builder()
                .name(screenName)
                .format(format)
                .theater(theater)
                .maxCols(maxCols)
                .maxRows(maxRows)
                .build();
    }

    public Movie createMovie(String title, String director, List<FilmFormat> formats, List<String> actors) {
        Movie movie = Movie.builder()
                .title(title)
                .director(director)
                .actors(actors)
                .formats(formats)
                .filmRating(FilmRating.G_RATED)
                .genres(Arrays.asList(MovieGenre.values()[0], MovieGenre.values()[1]))
                .image(title + ".jpg")
                .intro(title + " information")
                .nation("KR")
                .runningTime(160)
                .releaseDate(LocalDate.now().plusDays(10))
                .build();
        em.persist(movie);
        return movie;
    }

    public Movie createBasicMovie() {
        Movie movie = Movie.builder()
                .title("제목1")
                .director("감독1")
                .actors(List.of("배우1", "배우2", "배우3"))
                .formats(List.of(FilmFormat.TWO_D))
                .filmRating(FilmRating.G_RATED)
                .genres(Arrays.asList(MovieGenre.values()[0], MovieGenre.values()[1]))
                .image("제목1.jpg")
                .intro("제목1 information")
                .nation("KR")
                .runningTime(160)
                .releaseDate(LocalDate.now().plusDays(10))
                .build();
        em.persist(movie);
        return movie;
    }

    public Review writeReview(Movie movie) {
        Review review = Review.writeReview()
                .writer("홍길동")
                .comment("리뷰 내용")
                .score(10F)
                .movie(movie)
                .build();
        return review;
    }

    public Review writeReview(Movie movie, String writer, float score) {
        Review review = Review.writeReview()
                .writer(writer)
                .comment("리뷰 내용")
                .score(score)
                .movie(movie)
                .build();
        return review;
    }

    public Member createMember() {
        Member member = Member.basicBuilder()
                .name("홍길동")
                .nickname("홍길동")
                .email("aaa@naver.com")
                .gender(GenderType.MALE)
                .birth(LocalDate.now())
                .build();
        em.persist(member);
        return member;
    }

    public Schedule saveSchedule(Movie movie, Screen screen, LocalDateTime dateTime) {
        Schedule schedule = Schedule.builder()
                .movie(movie)
                .screen(screen)
                .screenTime(new ScreenTime(dateTime, movie.getRunningTime()))
                .build();
        em.persist(schedule);
        return schedule;
    }
}
