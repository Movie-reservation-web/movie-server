package study.movie.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
