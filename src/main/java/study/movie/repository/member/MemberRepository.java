package study.movie.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
