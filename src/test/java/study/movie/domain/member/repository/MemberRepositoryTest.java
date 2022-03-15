package study.movie.domain.member.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.member.Member;
import study.movie.global.constants.EntityAttrConst.GenderType;

import java.time.LocalDate;

@SpringBootTest
@Transactional
@Commit
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("고객 저장 및 조회 테스트")
    void 고객_조회() throws Exception {
        // given
        Member member = Member.builder()
                .name("이규연")
                .birth(LocalDate.now())
                .gender(GenderType.MALE)
                .email("rbdus7174@naver.com")
                .nickname("kxuxeon")
                .build();

        Member savedMember = memberRepository.save(member);

        // when
        Member findMember = memberRepository.findById(savedMember.getId()).orElse(null);

        // then
        Assertions.assertThat(savedMember).isEqualTo(findMember);
    }

}