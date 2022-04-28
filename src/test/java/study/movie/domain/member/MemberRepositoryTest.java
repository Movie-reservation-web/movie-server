package study.movie.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 고객을_조회한다() {
        // given
        Member member = Member.builder()
                .name("홍길동")
                .email("honggildong@naver.com")
                .birth(LocalDate.of(1980, 3, 22))
                .build();
        Member savedMember = memberRepository.save(member);

        // when
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        // then
        Assertions.assertThat(findMember).isEqualTo(savedMember);
    }
}