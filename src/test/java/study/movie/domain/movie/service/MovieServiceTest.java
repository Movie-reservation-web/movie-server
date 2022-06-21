package study.movie.domain.movie.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.entity.Movie;
import study.movie.domain.movie.repository.MovieRepository;
import study.movie.domain.schedule.dto.response.MovieChartResponse;
import study.movie.global.utils.StringUtil;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static study.movie.domain.movie.dto.condition.MovieChartSortType.AUDIENCE_DESC;

@SpringBootTest
@Transactional
@Slf4j
class MovieServiceTest {
    @Autowired
    EntityManager em;

    @Autowired
    MovieService movieService;

    @Autowired
    MovieRepository movieRepository;

    private List<Movie> initialMovieList;
    @BeforeEach
    void setUp(){
        initialMovieList = movieRepository.findAll();
    }

    @Test
    @DisplayName("영화 차트 조회를 한다.")
    void 영화_차트_조회() {
        // given
        // 초기 데이터에 관객수 추가
        int audience = 10;
        for (Movie movie : initialMovieList) {
            movie.updateAudience(audience);
            audience += 10;
        }
        em.flush();

        // when
        // 초기 데이터 관객수 내림차순 정렬, 개봉 여부(전체)
        List<Movie> sortByAudience = initialMovieList.stream()
                .sorted(comparing(Movie::getAudience, reverseOrder()))
                .collect(Collectors.toList());

        List<MovieChartResponse> movieCharts = movieService.findMovieBySort(AUDIENCE_DESC, false);

        // then
        assertThat(movieCharts.size()).isEqualTo(sortByAudience.size());
        for (int i = 1; i < movieCharts.size(); i++) {
            assertThat(movieCharts.get(i).getId()).isEqualTo(sortByAudience.get(i).getId());
            assertThat(movieCharts.get(i).getReservationRate())
                    .isEqualTo(
                            StringUtil.convertDoubleToString(
                                    sortByAudience.get(i).getReservationRate()
                            )
                    );
        }
    }
}
