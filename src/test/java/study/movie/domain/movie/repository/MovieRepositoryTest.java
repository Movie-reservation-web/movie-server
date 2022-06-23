package study.movie.domain.movie.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.entity.FilmFormat;
import study.movie.domain.movie.entity.FilmRating;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.movie.entity.MovieGenre;
import study.movie.domain.schedule.repository.ScheduleRepository;
import study.movie.global.utils.NumberUtil;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static study.movie.domain.movie.dto.condition.MovieChartSortType.AUDIENCE_DESC;
import static study.movie.domain.movie.dto.condition.MovieChartSortType.SCORE_DESC;

@SpringBootTest
@Transactional
@Slf4j
class MovieRepositoryTest {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    EntityManager em;

    private List<Movie> initialMovieList;
    @BeforeEach
    void setUp(){
        initialMovieList = movieRepository.findAll();
    }

    @Test
    @DisplayName("영화를 저장한다.")
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
                .intro("영웅이 될 것인가 악당이 될 것인가")
                .image("이미지")
                .build();

        Movie savedMovie = movieRepository.save(movie);

        // when
        Movie findMovie = movieRepository.findById(savedMovie.getId()).get();

        // then
        assertThat(findMovie).isEqualTo(savedMovie);
    }

    @Test
    @DisplayName("감독 이름으로 영화를 조회한다.")
    void 영화_조회_감독이름() {
        // given
        // when
        Movie movieForFind = initialMovieList.get((int) NumberUtil.getRandomIndex(movieRepository.count()));
        String directorForFind = movieForFind.getDirector();

        List<Movie> findMovies = movieRepository.findByDirector(directorForFind);

        // then
        for (Movie findMovie : findMovies) {
            assertThat(findMovie.getDirector()).isEqualTo(directorForFind);
        }
    }

    @Test
    @DisplayName("배우 이름으로 영화를 조회한다.")
    void 영화_조회_배우이름() {
        // given
        // when
        Movie movieForFind = initialMovieList.get((int) NumberUtil.getRandomIndex(movieRepository.count()));
        String actorForFind = movieForFind.getActors().get(0);

        List<Movie> findMovies = movieRepository.findByActor(actorForFind);

        // then
        for (Movie findMovie : findMovies) {
            assertThat(findMovie.getActors()).contains(actorForFind);
        }
    }

    @Test
    @DisplayName("상영일정에 저장된 모든 영화를 조회(관객 많은 순)한다.")
    void 영화_조회_관객순_정렬() {
        // given
        // 초기 데이터에 관객수 추가
        int audience = 10;
        for (Movie movie : initialMovieList) {
            movie.updateAudience(audience);
            audience += 10;
        }
        em.flush();

        // when
        // 초기 데이터 관객수 내림차순 정렬
        List<Movie> sortByAudience = initialMovieList.stream()
                .sorted(comparing(Movie::getAudience, reverseOrder()))
                .collect(Collectors.toList());


        List<Movie> findMovies = movieRepository.findMovieBySort(AUDIENCE_DESC, false);

        // then
        assertThat(findMovies.size()).isEqualTo(sortByAudience.size());
        for (int i = 1; i < findMovies.size(); i++) {
            assertThat(findMovies.get(i).getTitle()).isEqualTo(sortByAudience.get(i).getTitle());
            assertThat(findMovies.get(i).getAudience()).isEqualTo(sortByAudience.get(i).getAudience());
            assertThat(findMovies.get(i).getAudience() <= findMovies.get(i - 1).getAudience()).isTrue();
        }
    }

    @Test
    @DisplayName("상영일정에 저장된 개봉한 영화를 조회(관객 많은 순)한다.")
    void 개봉한_영화_조회_관객순_정렬() {
        // given
        // 초기 데이터에 관객수 추가
        int audience = 10;
        for (Movie movie : initialMovieList) {
            movie.updateAudience(audience);
            audience += 10;
        }
        em.flush();

        // when
        // 초기 데이터 관객수 내림차순 정렬, 개봉 여부(true)
        List<Movie> sortByAudience = initialMovieList.stream()
                .filter(movie -> movie.getReleaseDate().isBefore(LocalDate.now()))
                .sorted(comparing(Movie::getAudience, reverseOrder()))
                .collect(Collectors.toList());

        List<Movie> findMovies = movieRepository.findMovieBySort(AUDIENCE_DESC, true);

        // then
        assertThat(findMovies.size()).isEqualTo(sortByAudience.size());
        for (int i = 1; i < findMovies.size(); i++) {
            assertThat(findMovies.get(i).getId()).isEqualTo(sortByAudience.get(i).getId());
            assertThat(findMovies.get(i).getAudience()).isEqualTo(sortByAudience.get(i).getAudience());
            assertThat(findMovies.get(i).getAudience() <= findMovies.get(i - 1).getAudience()).isTrue();
            assertThat(findMovies.get(i).getReleaseDate().isBefore(LocalDate.now())).isTrue();
        }
    }

    @Test
    @DisplayName("상영일정에 저장된 모든 영화를 조회(평점 높은 순)한다.")
    void 영화_조회_평점_정렬() {
        // given
        // 모든 영화 엔티티 평정 계산
        initialMovieList.forEach(Movie::calcAverageScore);
        em.flush();

        // when
        List<Movie> sortByScore = initialMovieList.stream()
                .sorted(comparing(Movie::getAvgScore, reverseOrder()))
                .collect(Collectors.toList());

        List<Movie> findMovies = movieRepository.findMovieBySort(SCORE_DESC, false);

        // then
        assertThat(findMovies.size()).isEqualTo(sortByScore.size());
        for (int i = 1; i < findMovies.size(); i++) {
            assertThat(findMovies.get(i).getId()).isEqualTo(sortByScore.get(i).getId());
            assertThat(findMovies.get(i).getAvgScore()).isEqualTo(sortByScore.get(i).getAvgScore());
            assertThat(findMovies.get(i).getAvgScore() <= findMovies.get(i - 1).getAvgScore()).isTrue();
        }
    }

    @Test
    @DisplayName("상영일정에 저장된 개봉한 영화를 조회(평점 높은 순)한다.")
    void 개봉한_영화_조회_평점_정렬() {
        // given
        // 모든 영화 엔티티 평정 계산
        initialMovieList.forEach(Movie::calcAverageScore);
        em.flush();

        // when
        List<Movie> sortByScore = initialMovieList.stream()
                .filter(movie -> movie.getReleaseDate().isBefore(LocalDate.now()))
                .sorted(comparing(Movie::getAvgScore, reverseOrder()))
                .collect(Collectors.toList());

        List<Movie> findMovies = movieRepository.findMovieBySort(SCORE_DESC, true);

        // then
        assertThat(findMovies.size()).isEqualTo(sortByScore.size());
        for (int i = 1; i < findMovies.size(); i++) {
            assertThat(findMovies.get(i).getId()).isEqualTo(sortByScore.get(i).getId());
            assertThat(findMovies.get(i).getAvgScore()).isEqualTo(sortByScore.get(i).getAvgScore());
            assertThat(findMovies.get(i).getAvgScore() <= findMovies.get(i - 1).getAvgScore()).isTrue();
            assertThat(findMovies.get(i).getReleaseDate().isBefore(LocalDate.now())).isTrue();
        }
    }

    @Test
    @DisplayName("상영일정에 저장된 미개봉 영화를 조회한다.")
    void 미개봉_영화_조회() {
        // given
        // when
        List<Movie> unreleasedMovies = initialMovieList.stream()
                .filter(movie -> movie.getReleaseDate().isAfter(LocalDate.now()))
                .sorted(comparing(Movie::getReleaseDate))
                .collect(Collectors.toList());

        List<Movie> findMovies = movieRepository.findUnreleasedMovies();

        // then
        assertThat(findMovies.size()).isEqualTo(unreleasedMovies.size());
        for (int i = 1; i < findMovies.size(); i++) {
            assertThat(findMovies.get(i).getId()).isEqualTo(unreleasedMovies.get(i).getId());
            assertThat(findMovies.get(i).getReleaseDate().isAfter(LocalDate.now())).isTrue();
        }
    }
}
