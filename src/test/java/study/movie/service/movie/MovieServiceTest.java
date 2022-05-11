package study.movie.service.movie;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.domain.member.Member;
import study.movie.domain.movie.*;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.schedule.ScreenTime;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.Screen;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.theater.Theater;
import study.movie.dto.movie.MovieSortType;
import study.movie.dto.movie.UpdateMovieRequest;
import study.movie.dto.schedule.response.MovieChartResponse;
import study.movie.dto.ticket.request.ReserveTicketRequest;
import study.movie.repository.movie.MovieRepository;
import study.movie.repository.movie.ReviewRepository;
import study.movie.repository.schedule.ScheduleRepository;
import study.movie.service.ticket.TicketService;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    TicketService ticketService;

    @Autowired
    InitService init;

    static Movie movie;
    static Review review1;
    static Review review2;

    //    @BeforeAll
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

        review1 = Review.writeReview()
                .movie(movie)
                .writer("홍길동")
                .score((float) 3.2)
                .comment("평가나쁨")
                .build();

        review2 = Review.writeReview()
                .movie(movie)
                .writer("고길동")
                .score((float) 2.8)
                .comment("평가좋음")
                .build();
    }


    @Test
    void updateMovie_filmRating() {
        //given
        String info = "더 배트맨 소개";
        String image = "더 배트맨 이미지";
        LocalDate releaseDate = LocalDate.of(2022, 3, 1);
        FilmRating rating = FilmRating.UNDETERMINED;
        Movie movie = Movie.builder()
                .title("더 배트맨")
                .runningTime(176)
                .director("맷 리브스")
                .actors(Arrays.asList("로버트 패틴슨", "폴 다노"))
                .genres(Arrays.asList(MovieGenre.ACTION, MovieGenre.ADVENTURE))
                .formats(Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX))
                .filmRating(rating)
                .nation("한국")
                .releaseDate(releaseDate)
                .info(info)
                .image(image)
                .build();
        em.persist(movie);

        //when
        String changedInfo = "영웅이 될 것인가 악당이 될 것인가";
        String changedImage = "TheBatman.jpg";
        LocalDate changedReleaseDate = LocalDate.of(2022, 3, 20);
        FilmRating changedRating = FilmRating.PG_12;
        UpdateMovieRequest updateMovieRequest = new UpdateMovieRequest(
                movie.getId(),
                changedRating,
                changedReleaseDate,
                changedInfo,
                changedImage);
        movieService.updateMovie(updateMovieRequest);
        em.flush();
        //then
        Assertions.assertThat(movie.getInfo()).isEqualTo(changedInfo);
        Assertions.assertThat(movie.getImage()).isEqualTo(changedImage);
        Assertions.assertThat(movie.getReleaseDate()).isEqualTo(changedReleaseDate);
        Assertions.assertThat(movie.getFilmRating()).isEqualTo(changedRating);
    }

//    @Test
//    public void updateMovie_releaseDate_image() throws Exception {
//        //given
//        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
//        PostIdResponse postIdResponse = movieService.saveMovie(movieRequest);
//
//        UpdateMovieRequest updateMovieRequest = new UpdateMovieRequest(
//                createMovieResponse.getId(),
//                FilmRating.UNDETERMINED,
//                LocalDate.of(2022, 4, 1),
//                "영웅이 될 것인가 악당이 될 것인가",
//                "이미지2");
//
//        //when
//        movieService.updateMovie(updateMovieRequest);
//
//        //then
//        assertEquals(movieRepository.getById(createMovieResponse.getId()).getFilmRating(), FilmRating.UNDETERMINED);
//        assertEquals(movieRepository.getById(createMovieResponse.getId()).getReleaseDate(), LocalDate.of(2022, 4, 1));
//        assertEquals(movieRepository.getById(createMovieResponse.getId()).getImage(), "이미지2");
//    }
//
//
//
//    @Test
//    public void deleteMovie() throws Exception {
//        //given
////        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
////        CreateMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);
////        CreateReviewRequest reviewRequest = new CreateReviewRequest(review1, createMovieResponse.getId());
////        CreateReviewResponse createReviewResponse = movieService.saveReview(reviewRequest);
////
////        //when
////        movieService.deleteMovie(createMovieResponse.getId());
////
////        //then
////        assertEquals(null, movieRepository.getById(createMovieResponse.getId()));
////        assertEquals(null, reviewRepository.getById(createReviewResponse.getId()));
//    }
//
//
//
//    @Test
//    public void findOneMovie() throws Exception {
//        //given
//        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
//        BasicMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);
//
//        //when
//        BasicMovieResponse findMovie = movieService.findOneMovie(createMovieResponse.getId());
//
//        //then
//        assertEquals(createMovieResponse.getId(), findMovie.getId());
//    }
//
//    @Test
//    public void findAllReviewByMovieId() throws Exception {
////        //given
////        CreateMovieRequest movieRequest = new CreateMovieRequest(movie);
////        CreateMovieResponse createMovieResponse = movieService.saveMovie(movieRequest);
////        CreateReviewRequest reviewRequest1 = new CreateReviewRequest(review1, createMovieResponse.getId());
////        CreateReviewRequest reviewRequest2 = new CreateReviewRequest(review2, createMovieResponse.getId());
////        movieService.saveReview(reviewRequest1);
////        movieService.saveReview(reviewRequest2);
////
////        //when
////        List<ReviewResponse> ReviewResponseList = movieService.findAllReviewByMovieId(createMovieResponse.getId());
////
////        //then
////        assertEquals(ReviewResponseList.get(0).getMovie().getTitle(), "더 배트맨");
////        assertEquals(ReviewResponseList.get(0).getComment(), "평가나쁨");
////        assertEquals(ReviewResponseList.get(1).getComment(), "평가좋음");
//    }
//
//    @Test
//    public void findByCondition() throws Exception {
//        //given
//        //movie1
//        String actor = "로버트 패틴슨";
//        String director = "샘 킴";
//
//        List<String> actors1 = Arrays.asList(actor, "폴 다노");
//        List<FilmFormat> types1 = Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX);
//        Movie movie = init.createMovie("더 배트맨", "맷 리브스", types1, actors1);
//
//        //movie2
//        List<String> actors2 = Arrays.asList(actor, "레이첼");
//        List<FilmFormat> types2 = Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX);
//        Movie movie2 = init.createMovie("어바웃타임", director, types2, actors2);
//
//
//        //movie3
//        List<String> actors3 = Arrays.asList("찰스 자비에", "울버린");
//        List<FilmFormat> types3 = Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX);
//        Movie movie3 = init.createMovie("엑스맨", director, types3, actors3);
//
//
//        //제목_더 배트맨
////        MovieSearchCond movieCondition1 = new MovieSearchCond(null, "더 배트맨", null);
////        //배우명_찰스 자비에
////        MovieSearchCond movieCondition2 = new MovieSearchCond(null, null, "찰스 자비에");
////        //감독명&배우명_샘 킴,찰스 자비에
////        MovieSearchCond movieCondition3 = new MovieSearchCond("샘 킴", null, "찰스 자비에");
////        //감독명&배우명&제목_맷 리브스,폴 다노,더 배트맨
////        MovieSearchCond movieCondition4 = new MovieSearchCond("맷 리브스", "더 배트맨", "폴 다노");
//
//        //when
//        List<FindMovieResponse> conResult1 = movieService.findMovieByActor(actor);
//        List<FindMovieResponse> conResult2 = movieService.findMovieByDirector(director);
//
//        //then
//        //con1
//        assertEquals(conResult1.size(), 2);
//        assertEquals(conResult2.get(0).getTitle(), "더 배트맨");
//        //con2
//        assertEquals(conResult2.size(), 2);
//        assertEquals(conResult2.get(0).getTitle(), "어바웃타임");
//        assertEquals(conResult2.get(1).getTitle(), "엑스맨");
//    }
//
//    @Test
//    public void findUnreleasedMovies() throws Exception {
//        //given
//        //movie1
//        List<String> actors1 = Arrays.asList("로버트 패틴슨", "폴 다노");
//        List<MovieGenre> genres1 = Arrays.asList(MovieGenre.ACTION, MovieGenre.ADVENTURE);
//        List<FilmFormat> types1 = Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX);
//
//        Movie movie1 = Movie.builder()
//                .title("더 배트맨")
//                .runningTime(176)
//                .director("맷 리브스")
//                .actors(actors1)
//                .genres(genres1)
//                .formats(types1)
//                .filmRating(FilmRating.UNDETERMINED)
//                .nation("한국")
//                .releaseDate(LocalDate.of(2022, 3, 1))
//                .info("영웅이 될 것인가 악당이 될 것인가")
//                .image("이미지")
//                .build();
//
//        CreateMovieRequest movieRequest1 = new CreateMovieRequest(movie1);
//        BasicMovieResponse createMovieResponse1 = movieService.saveMovie(movieRequest1);
//
//        //movie2
//        List<String> actors2 = Arrays.asList("로버트 패틴슨", "레이첼");
//        List<MovieGenre> genres2 = Arrays.asList(MovieGenre.ACTION, MovieGenre.ADVENTURE);
//        List<FilmFormat> types2 = Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX);
//
//        Movie movie2 = Movie.builder()
//                .title("어바웃타임")
//                .runningTime(176)
//                .director("샘 킴")
//                .actors(actors2)
//                .genres(genres2)
//                .formats(types2)
//                .filmRating(FilmRating.UNDETERMINED)
//                .nation("한국")
//                .releaseDate(LocalDate.of(2022, 6, 1))
//                .info("영웅이 될 것인가 악당이 될 것인가")
//                .image("이미지")
//                .build();
//
//        CreateMovieRequest movieRequest2 = new CreateMovieRequest(movie2);
//        BasicMovieResponse createMovieResponse2 = movieService.saveMovie(movieRequest2);
//
//        //movie3
//        List<String> actors3 = Arrays.asList("찰스 자비에", "울버린");
//        List<MovieGenre> genres3 = Arrays.asList(MovieGenre.ACTION, MovieGenre.ADVENTURE);
//        List<FilmFormat> types3 = Arrays.asList(FilmFormat.IMAX, FilmFormat.FOUR_D_FLEX);
//
//        Movie movie3 = Movie.builder()
//                .title("엑스맨")
//                .runningTime(176)
//                .director("샘 킴")
//                .actors(actors3)
//                .genres(genres3)
//                .formats(types3)
//                .filmRating(FilmRating.UNDETERMINED)
//                .nation("한국")
//                .releaseDate(LocalDate.of(2023, 5, 1))
//                .info("영웅이 될 것인가 악당이 될 것인가")
//                .image("이미지")
//                .build();
//
//        CreateMovieRequest movieRequest3 = new CreateMovieRequest(movie3);
//        BasicMovieResponse createMovieResponse3 = movieService.saveMovie(movieRequest3);
//
//        //when
//        List<MovieChartResponse> list = movieService.findUnreleasedMovies();
//
//        //then
//        assertEquals(list.size(), 2);
//    }

    @Test
    void 상영중인_영화_차트() {
        // given
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie1 = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D), Arrays.asList("배우1"));
        Movie movie2 = init.createMovie("영화2", "홍길동", Arrays.asList(FilmFormat.TWO_D), Arrays.asList("배우2"));

        ScreenTime screenTime = new ScreenTime(LocalDateTime.now().plusDays(5), movie1.getRunningTime());
        ScreenTime screenTime1 = new ScreenTime(LocalDateTime.now().plusDays(4), movie1.getRunningTime());
        Schedule savedSchedule = Schedule.builder()
                .screenTime(screenTime)
                .screen(screen)
                .movie(movie1)
                .build();
        Schedule savedSchedule2 = Schedule.builder()
                .screenTime(screenTime1)
                .screen(screen)
                .movie(movie2)
                .build();
        scheduleRepository.save(savedSchedule);
        scheduleRepository.save(savedSchedule2);

        // when
        int movie1View = 10;
        int movie2View = 20;
        movie1.addAudience(movie1View);
        movie2.addAudience(movie2View);
        em.flush();
        em.clear();
        String movie1Rate = String.format("%.1f", (double) movie1View / (double) 30 * 100.0) + "%";
        String movie2Rate = String.format("%.1f", (double) movie2View / (double) 30 * 100.0) + "%";

        List<MovieChartResponse> movieCharts = movieService.findMovieBySort(MovieSortType.AUDIENCE_DESC, false);

        // then
        log.info("movieCharts={}", movieCharts);
        assertThat(movieCharts.size()).isEqualTo(2);
        assertThat(movieCharts.get(0).getId()).isEqualTo(movie2.getId());
        assertThat(movieCharts.get(0).getReservationRate()).isEqualTo(movie2Rate);
        assertThat(movieCharts.get(1).getId()).isEqualTo(movie1.getId());
        assertThat(movieCharts.get(1).getReservationRate()).isEqualTo(movie1Rate);
    }

    @Test
    void 영화_관람객_수_증가() {
        // given
        Member member = init.createMember();
        Theater theater = init.createTheater("CGV 용산", CityCode.SEL);
        Screen screen = init.registerScreen("1관", ScreenFormat.TWO_D, theater, 3, 3);
        Movie movie1 = init.createMovie("영화1", "홍길동", Arrays.asList(FilmFormat.TWO_D), Arrays.asList("배우1"));
        Movie movie2 = init.createMovie("영화2", "홍길동", Arrays.asList(FilmFormat.TWO_D), Arrays.asList("배우1"));
        LocalDate screenDate = LocalDate.of(2022, 3, 10);
        ScreenTime screenTime1 = new ScreenTime(screenDate.atTime(3, 2, 21), movie1.getRunningTime());
        ScreenTime screenTime2 = new ScreenTime(screenDate.atTime(10, 2, 21), movie1.getRunningTime());
        Schedule schedule1 = Schedule.builder()
                .screenTime(screenTime1)
                .screen(screen)
                .movie(movie1)
                .build();
        Schedule schedule2 = Schedule.builder()
                .screenTime(screenTime2)
                .screen(screen)
                .movie(movie2)
                .build();
        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);

        ReserveTicketRequest request1 = new ReserveTicketRequest();
        request1.setMemberId(member.getId());
        request1.setScheduleId(schedule1.getId());
        request1.setSeats(Arrays.asList("A2", "C2"));
        ReserveTicketRequest request2 = new ReserveTicketRequest();
        request2.setMemberId(member.getId());
        request2.setScheduleId(schedule1.getId());
        request2.setSeats(Arrays.asList("A2", "C2", "B1"));

        ReserveTicketRequest request3 = new ReserveTicketRequest();
        request3.setMemberId(member.getId());
        request3.setScheduleId(schedule2.getId());
        request3.setSeats(Arrays.asList("A2", "C2", "B1", "A1"));

        ticketService.reserve(request1);
        ticketService.reserve(request2);
        ticketService.reserve(request3);

        // when
        movieService.updateMovieAudience();

        // then
        assertThat(movie1.getAudience()).isEqualTo(5);
        assertThat(movie2.getAudience()).isEqualTo(4);
    }
}