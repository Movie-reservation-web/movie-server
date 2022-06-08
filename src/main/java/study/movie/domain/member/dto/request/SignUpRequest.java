package study.movie.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.movie.domain.member.entity.GenderType;
import study.movie.domain.member.entity.Member;
import study.movie.domain.member.entity.Role;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class SignUpRequest {
    @NotBlank
    @Pattern(regexp = "[a-zA-Z가-힣]{2,20}")
    private String name;

    @NotBlank
    @Pattern(regexp = "[a-z0-9A-Z._-]*@[a-z0-9A-Z]*.[a-zA-Z.]*")
    private String email;

    @NotBlank
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}")
    private String password;

    @NotBlank
    @Size(max = 20)
    private String nickname;

    @NotNull
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birth;

    @Pattern(regexp = "\\d{3}-\\d{3,4}-\\d{4}")
    private String mobile;

    @NotNull
    private GenderType gender;

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.basicBuilder()
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .name(this.name)
                .nickname(this.nickname)
                .birth(this.birth)
                .gender(this.gender)
                .mobile(this.mobile)
                .role(Role.USER)
                .build();
    }
}
