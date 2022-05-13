package study.movie.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.movie.movie.entity.FilmFormat;
import study.movie.movie.entity.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long>, MovieRepositoryCustom {

    /**
     * 감독으로 영화 조회
     *
     * @param director
     * @return List
     */
    List<Movie> findByDirector(String director);

    /**
     * 배우로 영화 조회
     *
     * @param actor
     * @return List
     */
    @Query(value = "select * from movie where movie.actors like %:actor%", nativeQuery = true)
    List<Movie> findByActor(@Param("actor") String actor);

    /**
     * 배우로 영화 조회(페이징)
     *
     * @param actor
     * @return Page
     */
    @Query(value = "select * from movie where movie.actors like %:actor%",
            countQuery = "select count(movie.movie_id) from movie where movie.actors like %:actor%",
            nativeQuery = true)
    Page<Movie> findByActorPaging(@Param("actor") String actor, Pageable pageable);

    /**
     * 포멧으로 영화 조회
     *
     * @param format
     * @return List
     */
    @Query(value = "select * from movie where movie.formats like %:format%",
            countQuery = "select count(movie.movie_id) from movie where movie.formats like %:format%",
            nativeQuery = true)
    Page<Movie> findByFormatPaging(@Param("format") FilmFormat format, Pageable pageable);
}
