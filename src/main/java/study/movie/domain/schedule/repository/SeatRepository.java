package study.movie.domain.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.schedule.entity.SeatEntity;

import java.util.List;

public interface SeatRepository extends JpaRepository<SeatEntity, Long> {

    @Transactional
    @Modifying
    @Query("delete from SeatEntity s where s.schedule.id in :ids")
    void deleteAllByScheduleIdInQuery(@Param("ids") List<Long> ids);
}
