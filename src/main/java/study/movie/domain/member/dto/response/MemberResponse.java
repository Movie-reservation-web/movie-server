package study.movie.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import study.movie.domain.member.entity.Member;
import study.movie.domain.member.entity.Role;

@Getter
@Builder
public class MemberResponse {
    private Long id;
    private String email;
    private Role roles;

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .roles(member.getRole())
                .build();
    }
}
