package study.movie.domain.movie;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.global.exception.CustomException;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Slf4j
public class MovieTest {

    @Autowired
    EntityManager em;

    @Autowired
    InitService init;

    @Test
    public void 영화_평점_계산() {
        // given
        Movie movie = init.createBasicMovie();
        Review review1 = init.writeReview(movie, "작성자1", 10f);
        Review review2 = init.writeReview(movie, "작성자2", 8f);

        //when

        movie.calcAverageScore();
        double avgScore = (review1.getScore() + review2.getScore()) / 2;
        String avgScoreToString = String.format("%.1f", avgScore);

        //then
        assertThat(movie.getAvgScore()).isEqualTo(avgScore);
        assertThat(movie.getAvgScoreToString()).isEqualTo(avgScoreToString);
    }

    @Test
    public void 영화_정보_수정() {
        // given
        FilmRating rating = FilmRating.UNDETERMINED;
        LocalDate releaseDate = LocalDate.of(2022, 5, 12);
        String info = "더 배트맨 소개";
        String image = "더 배트맨 이미지";
        Movie movie = Movie.builder()
                .title("더 배트맨")
                .director("맷 리브스")
                .actors(Arrays.asList("로버트 패틴슨", "폴 다노"))
                .formats(Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX))
                .filmRating(rating)
                .genres(Arrays.asList(MovieGenre.ACTION, MovieGenre.ADVENTURE))
                .image(image)
                .info(info)
                .nation("KR")
                .runningTime(176)
                .releaseDate(releaseDate)
                .build();
        em.persist(movie);
        Long savedMovieId = movie.getId();

        //when
        FilmRating changeRating = FilmRating.PG_12;
        LocalDate changeReleaseDate = LocalDate.of(2022, 5, 15);
        String changeInfo = "영웅이 될 것인가 악당이 될 것인가";
        String changeImage = "TheBatman.jpg";
        movie.update(changeRating, changeReleaseDate, changeInfo, changeImage);
        em.flush();
        em.clear();

        Movie findMovie = em.find(Movie.class, savedMovieId);

        //then
        assertThat(findMovie.getImage()).isEqualTo(changeImage);
        assertThat(findMovie.getReleaseDate()).isEqualTo(changeReleaseDate);
        assertThat(findMovie.getInfo()).isEqualTo(changeInfo);
        assertThat(findMovie.getFilmRating()).isEqualTo(changeRating);
    }

    @Test
    public void 영화_관객_추가_제거() {
        // given
        Movie theBatman = init.createMovie("더 배트맨", "맷 리브스",
                Arrays.asList(FilmFormat.TWO_D, FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX),
                Arrays.asList("로버트 패틴슨", "폴 다노"));
        Movie aboutTime = init.createMovie("어바웃 타임", "리차드 커티스",
                Arrays.asList(FilmFormat.TWO_D),
                Arrays.asList("도널 글리슨", "레이첼 맥아담스"));

        //when
        long beforeBatmanAudience = theBatman.getAudience();
        theBatman.addAudience(100);
        long addBatmanAudience = theBatman.getAudience();
        theBatman.dropAudience(50);
        long dropBatmanAudience = theBatman.getAudience();

        //then
        assertThat(beforeBatmanAudience).isEqualTo(0);
        assertThat(addBatmanAudience).isEqualTo(100);
        assertThat(dropBatmanAudience).isEqualTo(50);
        assertThatThrownBy(() -> aboutTime.dropAudience(10))
                .isInstanceOf(CustomException.class);
    }
}
