package study.movie.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.member.entity.Member;
import study.movie.domain.member.entity.SocialType;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndSocialType(String email, SocialType socialType);
    boolean existsByEmail(String email);
}
