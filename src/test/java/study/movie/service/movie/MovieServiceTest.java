package study.movie.service.movie;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.domain.movie.*;
import study.movie.dto.movie.*;
import study.movie.repository.movie.MovieRepository;
import study.movie.repository.movie.ReviewRepository;

import javax.persistence.EntityManager;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MovieServiceTest {
    @Autowired
    EntityManager em;

    @Autowired
    MovieService movieService;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    InitService init;

    static Movie movie;
    static Review review1;
    static Review review2;

    @BeforeAll
    public static void before() {
        List<String> actors = Arrays.asList("로버트 패틴슨", "폴 다노");
        List<MovieGenre> genres = Arrays.asList(MovieGenre.ACTION, MovieGenre.ADVENTURE);
        List<FilmFormat> types = Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX);

        movie = Movie.builder()
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

        review1 = Review.builder()
                .movie(movie)
                .writer("홍길동")
                .score((float) 3.2)
                .comment("평가나쁨")
                .build();

        review2 = Review.builder()
                .movie(movie)
                .writer("고길동")
                .score((float) 2.8)
                .comment("평가좋음")
                .build();
    }

    @Test
    public void saveMovie() throws Exception {
        //when
        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
        CreateMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);

        //then
        assertEquals(movieRequest.getTitle(), createMovieResponse.getTitle());
    }

    @Test
    public void saveReview() throws Exception {
        //when
        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
        CreateMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);
        CreateReviewRequest reviewRequest = new CreateReviewRequest(review1, createMovieResponse.getId());
        CreateReviewResponse createReviewResponse = movieService.saveReview(reviewRequest);

        //then
        assertEquals(reviewRequest.getMovieId(), createReviewResponse.getMovie().getId());
        assertEquals(reviewRequest.getComment(), createReviewResponse.getComment());
    }

    @Test
    public void updateMovie_filmRating() throws Exception{
        //given
        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
        CreateMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);

        UpdateMovieRequest updateMovieRequest = new UpdateMovieRequest(
                createMovieResponse.getId(),
                FilmRating.G_RATED,
                LocalDate.of(2022, 3, 1),
                "영웅이 될 것인가 악당이 될 것인가",
                "이미지");

        //when
        movieService.updateMovie(updateMovieRequest);

        //then
        assertEquals(movieRepository.getById(createMovieResponse.getId()).getFilmRating(), FilmRating.G_RATED);
        assertEquals(movieRepository.getById(createMovieResponse.getId()).getImage(), "이미지");
    }

    @Test
    public void updateMovie_releaseDate_image() throws Exception{
        //given
        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
        CreateMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);

        UpdateMovieRequest updateMovieRequest = new UpdateMovieRequest(
                createMovieResponse.getId(),
                FilmRating.UNDETERMINED,
                LocalDate.of(2022, 4, 1),
                "영웅이 될 것인가 악당이 될 것인가",
                "이미지2");

        //when
        movieService.updateMovie(updateMovieRequest);

        //then
        assertEquals(movieRepository.getById(createMovieResponse.getId()).getFilmRating(), FilmRating.UNDETERMINED);
        assertEquals(movieRepository.getById(createMovieResponse.getId()).getReleaseDate(), LocalDate.of(2022, 4, 1));
        assertEquals(movieRepository.getById(createMovieResponse.getId()).getImage(), "이미지2");
    }

    @Test
    public void updateReview() throws Exception{
        //given
        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
        CreateMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);
        CreateReviewRequest reviewRequest = new CreateReviewRequest(review1, createMovieResponse.getId());
        CreateReviewResponse createReviewResponse = movieService.saveReview(reviewRequest);

        UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest(createReviewResponse.getId(), 3.3F, "취소");

        //when
        movieService.updateReview(updateReviewRequest);

        //then
        assertEquals(reviewRepository.getById(createReviewResponse.getId()).getScore(), 3.3F);
        assertEquals(reviewRepository.getById(createReviewResponse.getId()).getComment(), "취소");
    }

    @Test
    public void deleteMovie() throws Exception{
        //given
        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
        CreateMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);
        CreateReviewRequest reviewRequest = new CreateReviewRequest(review1, createMovieResponse.getId());
        CreateReviewResponse createReviewResponse = movieService.saveReview(reviewRequest);

        //when
        movieService.deleteMovie(createMovieResponse.getId());

        //then
        assertEquals(null, movieRepository.getById(createMovieResponse.getId()));
        assertEquals(null, reviewRepository.getById(createReviewResponse.getId()));
    }

    @Test
    public void deleteReview() throws Exception{
        //given
        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
        CreateMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);
        CreateReviewRequest reviewRequest1 = new CreateReviewRequest(review1, createMovieResponse.getId());
        CreateReviewRequest reviewRequest2 = new CreateReviewRequest(review2, createMovieResponse.getId());
        CreateReviewResponse createReviewResponse1 = movieService.saveReview(reviewRequest1);
        CreateReviewResponse createReviewResponse2 = movieService.saveReview(reviewRequest2);

        //when
        movieService.deleteReview(createReviewResponse1.getId());

        //then
        assertEquals(1, movieRepository.getById(createMovieResponse.getId()).getReviewCount());
    }

    @Test
    public void findOneMovie() throws Exception{
        //given
        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
        CreateMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);

        //when
        CreateMovieResponse findMovie = movieService.findOneMovie(createMovieResponse.getId());

        //then
        assertEquals(createMovieResponse.getId(), findMovie.getId());
    }

    @Test
    public void findAllReviewByMovieId() throws Exception{
        //given
        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
        CreateMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);
        CreateReviewRequest reviewRequest1 = new CreateReviewRequest(review1, createMovieResponse.getId());
        CreateReviewRequest reviewRequest2 = new CreateReviewRequest(review2, createMovieResponse.getId());
        movieService.saveReview(reviewRequest1);
        movieService.saveReview(reviewRequest2);

        //when
        List<ReviewResponse> ReviewResponseList = movieService.findAllReviewByMovieId(createMovieResponse.getId());

        //then
        assertEquals(ReviewResponseList.get(0).getMovie().getTitle(), "더 배트맨");
        assertEquals(ReviewResponseList.get(0).getComment(), "평가나쁨");
        assertEquals(ReviewResponseList.get(1).getComment(), "평가좋음");
    }

    @Test
    public void findByCondition() throws Exception{

    }

}