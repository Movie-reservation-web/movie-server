package study.movie.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
