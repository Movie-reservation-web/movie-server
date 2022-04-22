package study.movie.repository.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.movie.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByWriter(String writer);

    @Modifying
    @Transactional
    @Query("delete from Review r where r.id = :id")
    void deleteByIdEqQuery(@Param("id") Long id);

}
