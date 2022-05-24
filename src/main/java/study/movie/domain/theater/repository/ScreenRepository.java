package study.movie.domain.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.theater.entity.Screen;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ScreenRepository extends JpaRepository<Screen, Long>, ScreenRepositoryCustom {
    @Modifying
    @Transactional
    @Query("delete from Screen s where s.id = :id")
    void deleteByIdEqQuery(@Param("id") Long id);
}
