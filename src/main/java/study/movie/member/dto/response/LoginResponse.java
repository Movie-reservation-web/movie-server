package study.movie.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import study.movie.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class LoginResponse {
    private Long id;
    private List<String> roles;
    private LocalDateTime createDateTime;

    public static LoginResponse of(Member member) {
        return LoginResponse.builder()
                .id(member.getId())
                .roles(member.getRoles())
                .createDateTime(member.getCreatedDate())
                .build();
    }

}
