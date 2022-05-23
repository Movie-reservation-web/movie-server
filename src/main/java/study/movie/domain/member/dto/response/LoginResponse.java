package study.movie.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import study.movie.domain.member.entity.Member;
import study.movie.domain.member.entity.Role;

import java.time.LocalDateTime;

@Getter
@Builder
public class LoginResponse {
    private Long id;
    private Role roles;
    private LocalDateTime createDateTime;

    public static LoginResponse of(Member member) {
        return LoginResponse.builder()
                .id(member.getId())
                .roles(member.getRole())
                .createDateTime(member.getCreatedDate())
                .build();
    }

}
