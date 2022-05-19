package study.movie.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import study.movie.member.entity.Member;

import java.util.List;

@Getter
@Builder
public class MemberResponse {
    private Long id;
    private String email;
    private List<String> roles;

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .roles(member.getRoles())
                .build();
    }
}
