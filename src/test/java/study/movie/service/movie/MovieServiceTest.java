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
        BasicMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);

        //then
        assertEquals(movieRequest.getTitle(), createMovieResponse.getTitle());
    }

    @Test
    public void saveReview() throws Exception {
        //when
        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
        BasicMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);
        CreateReviewRequest reviewRequest = new CreateReviewRequest();
//        reviewRequest.setMovieId(createMovieResponse.getId());
//        reviewRequest.setWriter(reviewRequest.getWriter());
//        reviewRequest.setComment(reviewRequest.getComment());
//        reviewRequest.setScore(reviewRequest.getScore());

        Long reviewId = movieService.saveReview(reviewRequest);

        //then
        assertEquals(reviewRequest.getMovieId(),reviewId);
    }

    @Test
    public void updateMovie_filmRating() throws Exception{
        //given
        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
        BasicMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);

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
        BasicMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);

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
//        //given
//        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
//        CreateMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);
//        CreateReviewRequest reviewRequest = new CreateReviewRequest();
//        CreateReviewResponse createReviewResponse = movieService.saveReview(reviewRequest);
//        reviewRequest.setMovieId(createMovieResponse.getId());
//        reviewRequest.setWriter(reviewRequest.getWriter());
//        reviewRequest.setComment(reviewRequest.getComment());
//        reviewRequest.setScore(reviewRequest.getScore());
//
//        UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest(createReviewResponse.getId(), 3.3F, "취소");
//
//        //when
//        movieService.updateReview(updateReviewRequest);
//
//        //then
//        assertEquals(reviewRepository.getById(createReviewResponse.getId()).getScore(), 3.3F);
//        assertEquals(reviewRepository.getById(createReviewResponse.getId()).getComment(), "취소");
    }

    @Test
    public void deleteMovie() throws Exception{
        //given
//        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
//        CreateMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);
//        CreateReviewRequest reviewRequest = new CreateReviewRequest(review1, createMovieResponse.getId());
//        CreateReviewResponse createReviewResponse = movieService.saveReview(reviewRequest);
//
//        //when
//        movieService.deleteMovie(createMovieResponse.getId());
//
//        //then
//        assertEquals(null, movieRepository.getById(createMovieResponse.getId()));
//        assertEquals(null, reviewRepository.getById(createReviewResponse.getId()));
    }

    @Test
    public void deleteReview() throws Exception{
        //given
//        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
//        CreateMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);
//        CreateReviewRequest reviewRequest1 = new CreateReviewRequest(review1, createMovieResponse.getId());
//        CreateReviewRequest reviewRequest2 = new CreateReviewRequest(review2, createMovieResponse.getId());
//        CreateReviewResponse createReviewResponse1 = movieService.saveReview(reviewRequest1);
//        CreateReviewResponse createReviewResponse2 = movieService.saveReview(reviewRequest2);
//
//        //when
//        movieService.deleteReview(createReviewResponse1.getId());
//
//        //then
//        assertEquals(1, movieRepository.getById(createMovieResponse.getId()).getReviewCount());
    }

    @Test
    public void findOneMovie() throws Exception{
        //given
        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
        BasicMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);

        //when
        BasicMovieResponse findMovie = movieService.findOneMovie(createMovieResponse.getId());

        //then
        assertEquals(createMovieResponse.getId(), findMovie.getId());
    }

    @Test
    public void findAllReviewByMovieId() throws Exception{
//        //given
//        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
//        CreateMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);
//        CreateReviewRequest reviewRequest1 = new CreateReviewRequest(review1, createMovieResponse.getId());
//        CreateReviewRequest reviewRequest2 = new CreateReviewRequest(review2, createMovieResponse.getId());
//        movieService.saveReview(reviewRequest1);
//        movieService.saveReview(reviewRequest2);
//
//        //when
//        List<ReviewResponse> ReviewResponseList = movieService.findAllReviewByMovieId(createMovieResponse.getId());
//
//        //then
//        assertEquals(ReviewResponseList.get(0).getMovie().getTitle(), "더 배트맨");
//        assertEquals(ReviewResponseList.get(0).getComment(), "평가나쁨");
//        assertEquals(ReviewResponseList.get(1).getComment(), "평가좋음");
    }

    @Test
    public void findByCondition() throws Exception{
        //given
        //movie1
        List<String> actors1 = Arrays.asList("로버트 패틴슨", "폴 다노");
        List<MovieGenre> genres1 = Arrays.asList(MovieGenre.ACTION, MovieGenre.ADVENTURE);
        List<FilmFormat> types1 = Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX);

        Movie movie1 = Movie.builder()
                .title("더 배트맨")
                .runningTime(176)
                .director("맷 리브스")
                .actors(actors1)
                .genres(genres1)
                .formats(types1)
                .filmRating(FilmRating.UNDETERMINED)
                .nation("한국")
                .releaseDate(LocalDate.of(2022, 3, 1))
                .info("영웅이 될 것인가 악당이 될 것인가")
                .image("이미지")
                .build();

        CreateMovieRequest movieRequest1 = new CreateMovieRequest(movie1);
        BasicMovieResponse createMovieResponse1 = movieService.saveMovie(movieRequest1);

        //movie2
        List<String> actors2 = Arrays.asList("로버트 패틴슨", "레이첼");
        List<MovieGenre> genres2 = Arrays.asList(MovieGenre.ACTION, MovieGenre.ADVENTURE);
        List<FilmFormat> types2 = Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX);

        Movie movie2 = Movie.builder()
                .title("어바웃타임")
                .runningTime(176)
                .director("샘 킴")
                .actors(actors2)
                .genres(genres2)
                .formats(types2)
                .filmRating(FilmRating.UNDETERMINED)
                .nation("한국")
                .releaseDate(LocalDate.of(2022, 6, 1))
                .info("영웅이 될 것인가 악당이 될 것인가")
                .image("이미지")
                .build();

        CreateMovieRequest movieRequest2 = new CreateMovieRequest(movie2);
        BasicMovieResponse createMovieResponse2 = movieService.saveMovie(movieRequest2);

        //movie3
        List<String> actors3 = Arrays.asList("찰스 자비에", "울버린");
        List<MovieGenre> genres3 = Arrays.asList(MovieGenre.ACTION, MovieGenre.ADVENTURE);
        List<FilmFormat> types3 = Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX);

        Movie movie3 = Movie.builder()
                .title("엑스맨")
                .runningTime(176)
                .director("샘 킴")
                .actors(actors3)
                .genres(genres3)
                .formats(types3)
                .filmRating(FilmRating.UNDETERMINED)
                .nation("한국")
                .releaseDate(LocalDate.of(2021, 5, 1))
                .info("영웅이 될 것인가 악당이 될 것인가")
                .image("이미지")
                .build();

        CreateMovieRequest movieRequest3 = new CreateMovieRequest(movie3);
        BasicMovieResponse createMovieResponse3 = movieService.saveMovie(movieRequest3);

        //movie4
        List<String> actors4 = Arrays.asList("찰스 자비에", "로건", "폴 다노");
        List<MovieGenre> genres4 = Arrays.asList(MovieGenre.ACTION, MovieGenre.ADVENTURE);
        List<FilmFormat> types4 = Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX);

        Movie movie4 = Movie.builder()
                .title("더 배트맨")
                .runningTime(176)
                .director("맷 리브스")
                .actors(actors4)
                .genres(genres4)
                .formats(types4)
                .filmRating(FilmRating.UNDETERMINED)
                .nation("한국")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .info("영웅이 될 것인가 악당이 될 것인가")
                .image("이미지")
                .build();

        CreateMovieRequest movieRequest4 = new CreateMovieRequest(movie4);
        BasicMovieResponse createMovieResponse4 = movieService.saveMovie(movieRequest4);

        //제목_더 배트맨
        MovieCondition movieCondition1 = new MovieCondition(null, "더 배트맨", null);
        //배우명_찰스 자비에
        MovieCondition movieCondition2 = new MovieCondition(null, null, "찰스 자비에");
        //감독명&배우명_샘 킴,찰스 자비에
        MovieCondition movieCondition3 = new MovieCondition("샘 킴", null, "찰스 자비에");
        //감독명&배우명&제목_맷 리브스,폴 다노,더 배트맨
        MovieCondition movieCondition4 = new MovieCondition("맷 리브스", "더 배트맨", "폴 다노");

        //when
        List<FindMovieResponse> conResult1 = movieService.findByCondition(movieCondition1);
        List<FindMovieResponse> conResult2 = movieService.findByCondition(movieCondition2);
        List<FindMovieResponse> conResult3 = movieService.findByCondition(movieCondition3);
        List<FindMovieResponse> conResult4 = movieService.findByCondition(movieCondition4);

        //then
        //con1
        assertEquals(conResult1.size(), 2);
        assertEquals(conResult1.get(0).getTitle(), "더 배트맨");
        //con2
        assertEquals(conResult2.size(), 2);
        assertEquals(conResult2.get(0).getTitle(), "엑스맨");
        assertEquals(conResult2.get(1).getTitle(), "더 배트맨");
        //con3
        assertEquals(conResult3.size(), 1);
        assertEquals(conResult3.get(0).getTitle(), "엑스맨");
        //con4
        assertEquals(conResult4.size(), 2);
        assertEquals(conResult4.get(0).getTitle(), "더 배트맨");
    }

    @Test
    public void findByOrderBy() throws Exception{
        //when
        //movieService.findByOrderBy();

        //then
    }

    @Test
    public void findUnreleasedMovies() throws Exception{
        //given
        //movie1
        List<String> actors1 = Arrays.asList("로버트 패틴슨", "폴 다노");
        List<MovieGenre> genres1 = Arrays.asList(MovieGenre.ACTION, MovieGenre.ADVENTURE);
        List<FilmFormat> types1 = Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX);

        Movie movie1 = Movie.builder()
                .title("더 배트맨")
                .runningTime(176)
                .director("맷 리브스")
                .actors(actors1)
                .genres(genres1)
                .formats(types1)
                .filmRating(FilmRating.UNDETERMINED)
                .nation("한국")
                .releaseDate(LocalDate.of(2022, 3, 1))
                .info("영웅이 될 것인가 악당이 될 것인가")
                .image("이미지")
                .build();

        CreateMovieRequest movieRequest1 = new CreateMovieRequest(movie1);
        BasicMovieResponse createMovieResponse1 = movieService.saveMovie(movieRequest1);

        //movie2
        List<String> actors2 = Arrays.asList("로버트 패틴슨", "레이첼");
        List<MovieGenre> genres2 = Arrays.asList(MovieGenre.ACTION, MovieGenre.ADVENTURE);
        List<FilmFormat> types2 = Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX);

        Movie movie2 = Movie.builder()
                .title("어바웃타임")
                .runningTime(176)
                .director("샘 킴")
                .actors(actors2)
                .genres(genres2)
                .formats(types2)
                .filmRating(FilmRating.UNDETERMINED)
                .nation("한국")
                .releaseDate(LocalDate.of(2022, 6, 1))
                .info("영웅이 될 것인가 악당이 될 것인가")
                .image("이미지")
                .build();

        CreateMovieRequest movieRequest2 = new CreateMovieRequest(movie2);
        BasicMovieResponse createMovieResponse2 = movieService.saveMovie(movieRequest2);

        //movie3
        List<String> actors3 = Arrays.asList("찰스 자비에", "울버린");
        List<MovieGenre> genres3 = Arrays.asList(MovieGenre.ACTION, MovieGenre.ADVENTURE);
        List<FilmFormat> types3 = Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX);

        Movie movie3 = Movie.builder()
                .title("엑스맨")
                .runningTime(176)
                .director("샘 킴")
                .actors(actors3)
                .genres(genres3)
                .formats(types3)
                .filmRating(FilmRating.UNDETERMINED)
                .nation("한국")
                .releaseDate(LocalDate.of(2023, 5, 1))
                .info("영웅이 될 것인가 악당이 될 것인가")
                .image("이미지")
                .build();

        CreateMovieRequest movieRequest3 = new CreateMovieRequest(movie3);
        BasicMovieResponse createMovieResponse3 = movieService.saveMovie(movieRequest3);

        //when
        List<FindMovieResponse> list = movieService.findUnreleasedMovies();

        //then
        assertEquals(list.size(), 2);
    }
}