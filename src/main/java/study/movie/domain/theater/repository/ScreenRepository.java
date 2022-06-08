package study.movie.domain.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.theater.entity.Screen;

import java.util.List;

public interface ScreenRepository extends JpaRepository<Screen, Long>, ScreenRepositoryCustom {
    @Modifying
    @Transactional
    @Query("delete from Screen s where s.id in :ids")
    void deleteByIdEqQuery(@Param("ids") List<Long> ids);
}
