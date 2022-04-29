package study.movie.service.movie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.repository.movie.MovieRepository;
import study.movie.repository.movie.ReviewRepository;

import javax.persistence.EntityManager;

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

//    @Test
//    public void 영화_생성() throws Exception {
//        saveMovie(movieRequest);
//
//    }

}