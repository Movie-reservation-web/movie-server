package study.movie.member.dto.request;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.movie.member.entity.GenderType;
import study.movie.member.entity.Member;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class SignUpRequest {
    @NotBlank
    @Pattern(regexp = "[a-zA-Z가-힣]{2,20}")
    private String name;

    @NotBlank
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}")
    private String password;

    @NotBlank
    @Size(max = 20)
    private String nickname;

    @NotBlank
    @Size(max = 20)
    private String emailId;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")
    private String emailDomain;

    @NotNull
    @Past
    private LocalDate birth;

    @NotNull
    private GenderType gender;

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(this.emailId + "@" + this.emailDomain)
                .password(passwordEncoder.encode(this.password))
                .name(this.name)
                .nickname(this.nickname)
                .birth(this.birth)
                .gender(this.gender)
                .build();
    }
}
