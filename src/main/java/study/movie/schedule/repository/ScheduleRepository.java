package study.movie.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import study.movie.schedule.entity.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {

    Optional<Schedule> findByScheduleNumber(String scheduleNumber);

    @Transactional
    @Modifying
    @Query("delete from Schedule s where s.id in :ids")
    void deleteAllByIdInQuery(@Param("ids") List<Long> ids);
}
